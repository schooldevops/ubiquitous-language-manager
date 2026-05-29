package com.aulms.candidate

import com.aulms.model.CandidatePromotionResult
import com.aulms.model.CandidateStatus
import com.aulms.model.ExpressionType
import com.aulms.model.SimilarTerm
import com.aulms.model.Term
import com.aulms.model.TermCandidate
import com.aulms.model.TermCandidateCreateRequest
import com.aulms.model.TermCandidateListResponse
import com.aulms.model.TermCandidatePromoteRequest
import com.aulms.model.TermCandidateReviewRequest
import com.aulms.model.TermStatus
import com.aulms.search.SearchNormalizer
import com.aulms.term.TermCommand
import com.aulms.term.TermRepository
import org.springframework.stereotype.Service

@Service
class CandidateService(
    private val candidateRepository: CandidateRepository,
    private val termRepository: TermRepository,
) {
    fun createCandidate(request: TermCandidateCreateRequest): TermCandidate =
        candidateRepository.create(request.toCommand(), similarTerms(request))

    fun listCandidates(q: String?, status: CandidateStatus?, domainName: String?, page: Int, size: Int): TermCandidateListResponse =
        candidateRepository.list(q, status, domainName, page, size)

    fun getCandidate(candidateId: String): TermCandidate = candidateRepository.get(candidateId)

    fun reviewCandidate(candidateId: String, request: TermCandidateReviewRequest): TermCandidate {
        val status = when (request.decision) {
            TermCandidateReviewRequest.Decision.Approve -> CandidateStatus.Approved
            TermCandidateReviewRequest.Decision.Reject -> CandidateStatus.Rejected
            TermCandidateReviewRequest.Decision.RequestChanges -> CandidateStatus.Reviewing
        }
        return candidateRepository.updateStatus(candidateId, status, request.reviewer, request.reason, reviewedBy = request.reviewer)
    }

    fun promoteCandidate(candidateId: String, request: TermCandidatePromoteRequest): CandidatePromotionResult {
        val candidate = candidateRepository.get(candidateId)
        if (candidate.status != CandidateStatus.Approved) {
            throw IllegalStateException("Only Approved candidate can be promoted: $candidateId")
        }
        val term = termRepository.create(
            TermCommand(
                domainName = candidate.domainName,
                usageType = "표준항목",
                koreanName = candidate.koreanName,
                englishName = candidate.englishName,
                englishAbbreviation = candidate.englishAbbreviation,
                businessDefinition = candidate.businessDefinition,
                usageContext = candidate.usageContext,
                physicalType = candidate.physicalType,
                digits = candidate.digits,
                decimalPoint = candidate.decimalPoint,
                owner = request.owner,
                status = TermStatus.Approved,
            ),
        )
        addPromotedExpressions(term)
        val promoted = candidateRepository.updateStatus(candidateId, CandidateStatus.Promoted, request.approver, request.reason, reviewedBy = request.approver, promotedTermId = term.termId)
        return CandidatePromotionResult(candidate = promoted, term = termRepository.get(term.termId))
    }

    private fun similarTerms(request: TermCandidateCreateRequest): List<SimilarTerm> {
        val normalizedKorean = SearchNormalizer.normalize(request.koreanName)
        val normalizedEnglish = SearchNormalizer.normalize(request.englishName)
        return termRepository.searchDocuments()
            .filter {
                it.term.domainName == request.domainName ||
                    SearchNormalizer.normalize(it.term.koreanName).contains(normalizedKorean) ||
                    normalizedKorean.contains(SearchNormalizer.normalize(it.term.koreanName)) ||
                    SearchNormalizer.normalize(it.term.englishName).contains(normalizedEnglish) ||
                    normalizedEnglish.contains(SearchNormalizer.normalize(it.term.englishName))
            }
            .take(5)
            .map {
                SimilarTerm(
                    termId = it.term.termId,
                    koreanName = it.term.koreanName,
                    englishName = it.term.englishName,
                    dbColumn = it.term.englishAbbreviation,
                    apiField = it.apiField(),
                    reason = if (it.term.domainName == request.domainName) "같은 ${request.domainName} 도메인의 기존 표준 용어" else "표현이 유사한 기존 표준 용어",
                )
            }
    }

    private fun addPromotedExpressions(term: Term) {
        val apiField = term.englishName.split(" ").map { it.lowercase() }.mapIndexed { index, part ->
            if (index == 0) part else part.replaceFirstChar { char -> char.uppercase() }
        }.joinToString("")
        termRepository.addExpression(term.termId, ExpressionType.Korean, term.koreanName, "ko", "standard", true)
        termRepository.addExpression(term.termId, ExpressionType.English, term.englishName, "en", "title", true)
        termRepository.addExpression(term.termId, ExpressionType.DB_COLUMN, term.englishAbbreviation, "en", "UPPER_SNAKE", true)
        termRepository.addExpression(term.termId, ExpressionType.API_FIELD, apiField, "en", "camelCase", true)
        termRepository.addExpression(term.termId, ExpressionType.CODE_VARIABLE, apiField, "en", "camelCase", true)
        termRepository.addExpression(term.termId, ExpressionType.UI_LABEL, term.koreanName, "ko", "label", true)
        termRepository.addExpression(term.termId, ExpressionType.TEST_WORD, term.koreanName, "ko", "gherkin", true)
    }
}

private fun TermCandidateCreateRequest.toCommand(): CandidateCommand = CandidateCommand(
    koreanName = koreanName,
    englishName = englishName,
    englishAbbreviation = englishAbbreviation,
    domainName = domainName,
    businessDefinition = businessDefinition,
    usageContext = usageContext,
    physicalType = physicalType,
    digits = digits,
    decimalPoint = decimalPoint,
    requestedBy = requestedBy,
)
