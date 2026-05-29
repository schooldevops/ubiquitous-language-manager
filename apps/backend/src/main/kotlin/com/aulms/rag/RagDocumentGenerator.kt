package com.aulms.rag

import com.aulms.model.AliasType
import com.aulms.model.CandidateStatus
import com.aulms.model.TermCandidate
import com.aulms.model.TermStatus
import com.aulms.term.TermRepository
import com.aulms.term.TermSearchDocument
import java.nio.file.Files
import java.nio.file.Path

class RagDocumentGenerator(private val termRepository: TermRepository) {
    fun generateTermDocuments(outputDirectory: Path): List<Path> {
        Files.createDirectories(outputDirectory)
        return termRepository.searchDocuments()
            .filter { it.term.status == TermStatus.Approved || it.term.status == TermStatus.Deprecated }
            .map { document ->
                val path = outputDirectory.resolve("${document.term.termId}-${slug(document.term.englishName)}.md")
                Files.writeString(path, renderTermDocument(document))
                path
            }
    }

    fun generateCandidateDocuments(outputDirectory: Path, candidates: List<TermCandidate>): List<Path> {
        Files.createDirectories(outputDirectory)
        return candidates
            .filter { it.status == CandidateStatus.Draft || it.status == CandidateStatus.Reviewing }
            .map { candidate ->
                val path = outputDirectory.resolve("${candidate.candidateId}-${slug(candidate.englishName)}.md")
                Files.writeString(path, renderCandidateDocument(candidate))
                path
            }
    }

    fun renderTermDocument(document: TermSearchDocument): String {
        val term = document.term
        val relationships = relationshipsFor(term.termId)
        val deprecatedNotice = if (term.status == TermStatus.Deprecated) {
            "\n## Deprecated 안내\n\n- 이 용어는 Deprecated 상태입니다.\n- 대체 표준 용어를 확인한 후 신규 산출물에는 사용하지 않습니다.\n"
        } else {
            ""
        }

        return """
            |---
            |termId: ${term.termId}
            |termNumber: ${term.termNumber}
            |domainName: ${term.domainName}
            |status: ${term.status.value}
            |englishName: "${term.englishName}"
            |dbColumn: ${term.englishAbbreviation}
            |apiField: ${document.apiField()}
            |codeVariable: ${document.codeVariable()}
            |---
            |
            |# ${term.koreanName}
            |
            |## 업무 정의
            |
            |${term.businessDefinition}
            |
            |## 사용 맥락
            |
            |${term.usageContext ?: "사용 맥락 미정의"}
            |
            |## 기본 정보
            |
            || 항목 | 값 |
            ||---|---|
            || 한글 표준 용어 | ${term.koreanName} |
            || 영문명 | ${term.englishName} |
            || DB 컬럼명 | ${term.englishAbbreviation} |
            || API 필드명 | ${document.apiField()} |
            || 코드 변수명 | ${document.codeVariable()} |
            || 물리 타입 | ${term.physicalType} |
            || 자릿수 | ${term.digits} |
            || 소수점 | ${term.decimalPoint} |
            || 소유자 | ${term.owner} |
            || 버전 | ${term.version} |
            |
            |## 산출물 표현
            |
            || 표현 유형 | 표현값 | 언어 | 스타일 | 표준 여부 |
            ||---|---|---|---|---|
            |${document.expressions.joinToString("\n") { "| ${it.expressionType.value} | ${it.expressionValue} | ${it.language ?: ""} | ${it.style ?: ""} | ${it.isStandard} |" }}
            |
            |## 유사어와 금지어
            |
            |${aliasesMarkdown(document)}
            |
            |## 관련 용어
            |
            |${if (relationships.isEmpty()) "- 관련 용어 미정의" else relationships.joinToString("\n") { "- ${it.relationshipType}: ${it.targetTermName} - ${it.description}" }}
            |
            |## 예시 문장
            |
            |${exampleSentences(document)}
            |$deprecatedNotice
        """.trimMargin()
    }

    fun renderCandidateDocument(candidate: TermCandidate): String = """
        |---
        |candidateId: ${candidate.candidateId}
        |domainName: ${candidate.domainName}
        |status: ${candidate.status.value}
        |englishName: "${candidate.englishName}"
        |dbColumn: ${candidate.englishAbbreviation}
        |---
        |
        |# ${candidate.koreanName}
        |
        |## 후보 정의
        |
        |${candidate.businessDefinition}
        |
        |## 사용 맥락
        |
        |${candidate.usageContext ?: "사용 맥락 미정의"}
        |
        |## 승인 필요
        |
        |- 상태: ${candidate.status.value}
        |- 요청자: ${candidate.requestedBy}
        |- 개발 반영 전 데이터 표준 승인 필요
        |
        |## 유사 기존 용어
        |
        |${if (candidate.similarTerms.isEmpty()) "- 유사 기존 용어 없음" else candidate.similarTerms.joinToString("\n") { "- ${it.koreanName} / ${it.englishName} / ${it.dbColumn} / ${it.apiField}: ${it.reason}" }}
    """.trimMargin()

    private fun aliasesMarkdown(document: TermSearchDocument): String =
        if (document.aliases.isEmpty()) {
            "- 유사어 또는 금지어 미정의"
        } else {
            document.aliases.joinToString("\n") {
                val label = when (it.aliasType) {
                    AliasType.Synonym -> "유사어"
                    AliasType.Forbidden -> "금지어"
                    AliasType.Deprecated -> "폐기어"
                    AliasType.NeedsContext -> "문맥 확인"
                }
                "- ${label}: ${it.aliasName} -> ${it.recommendationAction}. ${it.reason}"
            }
        }

    private fun exampleSentences(document: TermSearchDocument): String {
        val term = document.term
        return when (term.koreanName) {
            "고객번호" -> """
                |- 기획서: 고객번호를 입력하면 주문 목록을 조회한다.
                |- API: `GET /orders?customerNumber={customerNumber}`
                |- Kotlin: `val customerNumber: String`
                |- SQL: `WHERE CUST_NO = :custNo`
            """.trimMargin()
            "주문번호" -> """
                |- 기획서: 주문번호로 주문 상세를 조회한다.
                |- API: `orderNumber`
                |- SQL: `ORD_NO`
            """.trimMargin()
            else -> """
                |- 기획서: ${term.koreanName}를 사용해 ${term.domainName} 업무를 처리한다.
                |- API: `${document.apiField()}`
                |- SQL: `${term.englishAbbreviation}`
            """.trimMargin()
        }
    }

    private fun relationshipsFor(termId: String): List<RagRelationship> = relationships.filter { it.sourceTermId == termId }

    private fun slug(value: String): String = value.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')

    private val relationships = listOf(
        RagRelationship("T-000001", "relatedTo", "고객명", "고객번호는 고객명과 함께 고객 식별 정보로 사용"),
        RagRelationship("T-000001", "relatedTo", "고객상태코드", "고객번호는 고객상태코드와 함께 고객 상태 판단에 사용"),
        RagRelationship("T-000001", "usedWith", "주문번호", "고객번호는 주문번호와 함께 고객별 주문 조회에 사용"),
        RagRelationship("T-000004", "usedWith", "주문일자", "주문번호는 주문일자와 함께 주문 목록에 표시"),
        RagRelationship("T-000004", "usedWith", "주문금액", "주문번호는 주문금액과 함께 주문 목록에 표시"),
        RagRelationship("T-000004", "usedWith", "주문상태코드", "주문번호는 주문상태코드와 함께 주문 진행 상태 확인에 사용"),
    )
}

data class RagRelationship(
    val sourceTermId: String,
    val relationshipType: String,
    val targetTermName: String,
    val description: String,
)
