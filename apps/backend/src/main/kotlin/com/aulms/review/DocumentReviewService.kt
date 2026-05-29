package com.aulms.review

import com.aulms.model.AliasType
import com.aulms.model.CandidateTerm
import com.aulms.model.DetectedTerm
import com.aulms.model.DocumentReviewRequest
import com.aulms.model.DocumentReviewResult
import com.aulms.model.ExpressionType
import com.aulms.model.StandardMapping
import com.aulms.model.ValidationIssue
import com.aulms.model.ValidationSeverity
import com.aulms.rule.RuleEngine
import com.aulms.rule.RuleValidationRequest
import com.aulms.term.TermRepository
import com.aulms.term.TermSearchDocument
import org.springframework.stereotype.Service

@Service
class DocumentReviewService(
    private val repository: TermRepository,
    private val ruleEngine: RuleEngine,
) {
    fun review(request: DocumentReviewRequest): DocumentReviewResult {
        val sentences = splitSentences(request.documentText)
        val entries = reviewEntries(request.domainNames)
        val matches = detectTerms(request.documentText, sentences, entries)
        val recommendedText = replaceByStandardTerms(request.documentText, matches)
        val candidateTerms = if (request.options?.includeCandidateTerms ?: true) {
            candidateTerms(request.documentText, matches)
        } else {
            emptyList()
        }
        val validationIssues = if (request.options?.includeValidationIssues ?: true) {
            matches.flatMap { it.validationIssues() }.distinctBy { "${it.source}:${it.inputExpression}:${it.reason}" }
        } else {
            emptyList()
        }

        return DocumentReviewResult(
            originalText = request.documentText,
            recommendedText = if (request.options?.normalizeSentences == false) request.documentText else recommendedText,
            detectedTerms = matches.map { it.toDetectedTerm() },
            standardMappings = matches.map { it.toStandardMapping() }.distinctBy { "${it.inputExpression}:${it.termId}" },
            candidateTerms = candidateTerms,
            validationIssues = validationIssues,
        )
    }

    private fun splitSentences(text: String): List<SentenceRange> {
        val result = mutableListOf<SentenceRange>()
        var start = 0
        text.forEachIndexed { index, char ->
            if (char == '.' || char == '?' || char == '!' || char == '\n') {
                if (start <= index) {
                    result.add(SentenceRange(result.size, start, index + 1))
                }
                start = index + 1
            }
        }
        if (start < text.length) {
            result.add(SentenceRange(result.size, start, text.length))
        }
        return result.ifEmpty { listOf(SentenceRange(0, 0, text.length)) }
    }

    private fun reviewEntries(domainNames: List<String>?): List<ReviewEntry> =
        repository.searchDocuments()
            .filter { domainNames.isNullOrEmpty() || it.term.domainName in domainNames }
            .flatMap { document ->
                val standardEntries = document.expressions
                    .filter { it.expressionType in setOf(ExpressionType.Korean, ExpressionType.UI_LABEL, ExpressionType.TEST_WORD) }
                    .map {
                        ReviewEntry(
                            expression = it.expressionValue,
                            matchType = DetectedTerm.MatchType.Exact,
                            document = document,
                            reason = "${it.expressionValue}는 승인된 표준 표현입니다.",
                        )
                    }
                val aliasEntries = document.aliases.map {
                    ReviewEntry(
                        expression = it.aliasName,
                        matchType = if (it.aliasType == AliasType.Deprecated) DetectedTerm.MatchType.Alias else DetectedTerm.MatchType.Alias,
                        document = document,
                        reason = "${it.aliasName}는 ${document.term.koreanName}의 유사어입니다.",
                    )
                }
                standardEntries + aliasEntries
            }
            .sortedByDescending { it.expression.length }

    private fun detectTerms(text: String, sentences: List<SentenceRange>, entries: List<ReviewEntry>): List<ReviewMatch> {
        val occupied = mutableListOf<IntRange>()
        val matches = mutableListOf<ReviewMatch>()
        entries.forEach { entry ->
            entry.toFlexibleRegex().findAll(text).forEach { match ->
                val range = match.range
                if (occupied.none { it.overlaps(range) }) {
                    occupied.add(range)
                    matches.add(
                        ReviewMatch(
                            inputExpression = match.value,
                            startOffset = range.first,
                            endOffset = range.last + 1,
                            sentenceIndex = sentences.firstOrNull { range.first in it.startOffset until it.endOffset }?.index ?: 0,
                            entry = entry,
                        ),
                    )
                }
            }
        }
        return matches.sortedBy { it.startOffset }
    }

    private fun ReviewEntry.toFlexibleRegex(): Regex {
        val pattern = expression
            .trim()
            .map { Regex.escape(it.toString()) }
            .joinToString("[\\s_-]*")
        return Regex(pattern, RegexOption.IGNORE_CASE)
    }

    private fun replaceByStandardTerms(text: String, matches: List<ReviewMatch>): String {
        var result = text
        matches.sortedByDescending { it.startOffset }.forEach {
            val replacement = it.entry.document.displayTerm()
            val particle = result.getOrNull(it.endOffset)
            if (particle == '을' || particle == '를') {
                result = result.replaceRange(it.startOffset, it.endOffset + 1, replacement + objectParticle(replacement))
            } else {
                result = result.replaceRange(it.startOffset, it.endOffset, replacement)
            }
        }
        return result
    }

    private fun candidateTerms(text: String, matches: List<ReviewMatch>): List<CandidateTerm> {
        val matchedRanges = matches.map { it.startOffset until it.endOffset }
        val candidates = Regex("[가-힣A-Za-z][가-힣A-Za-z\\s_-]{1,20}")
            .findAll(text)
            .map { it.value.trim() to it.range }
            .filter { (_, range) -> matchedRanges.none { it.overlaps(range) } }
            .map { (value, _) -> value.trimEnd('을', '를', '은', '는', '이', '가') }
            .filter { it.length >= 2 && it !in documentStopWords }
            .distinct()
            .take(10)
            .toList()

        return candidates.map {
            CandidateTerm(
                expression = it,
                recommendedKoreanName = it.replace(Regex("\\s+"), ""),
                reason = "데이터 사전에서 매핑되는 표준 용어를 찾지 못했습니다.",
                approvalRequired = true,
            )
        }
    }

    private fun ReviewMatch.validationIssues(): List<ValidationIssue> =
        ruleEngine.validateExpression(
            RuleValidationRequest(
                source = "DOCUMENT",
                inputExpression = inputExpression,
                expressionType = ExpressionType.Korean,
                location = "sentence:${sentenceIndex + 1}",
                developmentUsage = false,
            ),
        ).ifEmpty {
            if (entry.matchType == DetectedTerm.MatchType.Exact) {
                emptyList()
            } else {
                listOf(
                    ValidationIssue(
                        severity = ValidationSeverity.WARNING,
                        source = "DOCUMENT",
                        inputExpression = inputExpression,
                        location = "sentence:${sentenceIndex + 1}",
                        standardTerm = entry.document.term.koreanName,
                        recommendedExpression = entry.document.term.koreanName,
                        reason = "${inputExpression}는 ${entry.document.term.koreanName}로 표준화해야 합니다.",
                    ),
                )
            }
        }

    private fun ReviewMatch.toDetectedTerm(): DetectedTerm = DetectedTerm(
        expression = inputExpression,
        sentenceIndex = sentenceIndex,
        startOffset = startOffset,
        endOffset = endOffset,
        matchType = entry.matchType,
        mappedTermId = entry.document.term.termId,
        standardTerm = entry.document.term.koreanName,
    )

    private fun ReviewMatch.toStandardMapping(): StandardMapping = StandardMapping(
        inputExpression = inputExpression,
        termId = entry.document.term.termId,
        standardTerm = entry.document.term.koreanName,
        englishName = entry.document.term.englishName,
        dbColumn = entry.document.term.englishAbbreviation,
        apiField = entry.document.apiField(),
        codeVariable = entry.document.codeVariable(),
        reason = entry.reason,
    )

    private fun TermSearchDocument.displayTerm(): String = when (term.koreanName) {
        "주문목록" -> "주문 목록"
        else -> term.koreanName
    }

    private fun objectParticle(value: String): String {
        val last = value.trim().lastOrNull() ?: return "를"
        if (last !in '가'..'힣') {
            return "를"
        }
        return if ((last.code - '가'.code) % 28 == 0) "를" else "을"
    }

    private fun IntRange.overlaps(other: IntRange): Boolean = first <= other.last && other.first <= last

    private data class SentenceRange(
        val index: Int,
        val startOffset: Int,
        val endOffset: Int,
    )

    private data class ReviewEntry(
        val expression: String,
        val matchType: DetectedTerm.MatchType,
        val document: TermSearchDocument,
        val reason: String,
    )

    private data class ReviewMatch(
        val inputExpression: String,
        val startOffset: Int,
        val endOffset: Int,
        val sentenceIndex: Int,
        val entry: ReviewEntry,
    )

    private companion object {
        val documentStopWords = setOf("입력하면", "조회한다", "회한다", "저장한다", "표시한다", "사용자는", "결과에는")
    }
}
