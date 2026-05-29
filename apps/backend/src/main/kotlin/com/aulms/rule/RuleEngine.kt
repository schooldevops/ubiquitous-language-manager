package com.aulms.rule

import com.aulms.model.AliasType
import com.aulms.model.ExpressionType
import com.aulms.model.TermExpression
import com.aulms.model.TermStatus
import com.aulms.model.ValidationIssue
import com.aulms.model.ValidationSeverity
import com.aulms.search.SearchNormalizer
import com.aulms.term.TermRepository
import com.aulms.term.TermSearchDocument
import org.springframework.stereotype.Component

@Component
class RuleEngine(private val repository: TermRepository) {
    fun validateExpression(request: RuleValidationRequest): List<ValidationIssue> {
        val normalizedInput = SearchNormalizer.normalize(request.inputExpression)
        val documents = repository.searchDocuments()

        val aliasMatch = documents.firstNotNullOfOrNull { document ->
            document.aliases.firstOrNull { SearchNormalizer.normalize(it.aliasName) == normalizedInput }?.let { document to it }
        }
        if (aliasMatch != null) {
            val (document, alias) = aliasMatch
            val severity = when (alias.aliasType) {
                AliasType.Synonym, AliasType.NeedsContext -> ValidationSeverity.WARNING
                AliasType.Forbidden, AliasType.Deprecated -> ValidationSeverity.ERROR
            }
            return listOf(
                request.issue(
                    severity = severity,
                    standardTerm = document.term.koreanName,
                    recommendedExpression = document.recommendedExpression(request.expressionType),
                    reason = "${alias.reason}. 권장 표현은 ${document.recommendedExpression(request.expressionType)}입니다.",
                ),
            )
        }

        val expressionMatch = documents.firstNotNullOfOrNull { document ->
            document.matchingExpression(normalizedInput, request.expressionType)?.let { document to it }
        }
        if (expressionMatch == null) {
            return listOf(
                request.issue(
                    severity = ValidationSeverity.INFO,
                    reason = "데이터 사전에서 일치하는 표준 용어 또는 별칭을 찾지 못했습니다. 신규 용어 후보 검토가 필요합니다.",
                ),
            )
        }

        val (document, expression) = expressionMatch
        val statusIssue = document.statusIssue(request)
        if (statusIssue != null) {
            return listOf(statusIssue)
        }

        if (expression.expressionType != request.expressionType) {
            return listOf(
                request.issue(
                    severity = ValidationSeverity.ERROR,
                    standardTerm = document.term.koreanName,
                    recommendedExpression = document.recommendedExpression(request.expressionType),
                    reason = "${document.term.koreanName}의 ${request.expressionType.value} 표준 표현은 ${document.recommendedExpression(request.expressionType)}입니다.",
                ),
            )
        }

        val expectedExpression = document.recommendedExpression(request.expressionType)
        if (SearchNormalizer.normalize(expectedExpression) != normalizedInput) {
            return listOf(
                request.issue(
                    severity = ValidationSeverity.WARNING,
                    standardTerm = document.term.koreanName,
                    recommendedExpression = expectedExpression,
                    reason = "${document.term.koreanName}의 권장 표현은 ${expectedExpression}입니다.",
                ),
            )
        }

        return emptyList()
    }

    fun validatePhysicalSpec(request: PhysicalSpecValidationRequest): List<ValidationIssue> {
        val document = repository.searchDocuments().firstOrNull { it.term.termId == request.termId }
            ?: return listOf(
                ValidationIssue(
                    severity = ValidationSeverity.INFO,
                    source = request.source,
                    inputExpression = request.termId,
                    location = request.location,
                    reason = "물리 스펙을 검증할 표준 용어를 찾지 못했습니다.",
                ),
            )

        val issues = mutableListOf<ValidationIssue>()
        if (!document.term.physicalType.equals(request.physicalType, ignoreCase = true)) {
            issues.add(request.issue(document, "물리 타입", document.term.physicalType, request.physicalType))
        }
        if (document.term.digits != request.digits) {
            issues.add(request.issue(document, "자릿수", document.term.digits.toString(), request.digits.toString()))
        }
        if (document.term.decimalPoint != request.decimalPoint) {
            issues.add(request.issue(document, "소수점", document.term.decimalPoint.toString(), request.decimalPoint.toString()))
        }

        return issues
    }

    private fun TermSearchDocument.matchingExpression(normalizedInput: String, requestedType: ExpressionType) =
        (expressions + coreExpressions())
            .firstOrNull { it.expressionType == requestedType && SearchNormalizer.normalize(it.expressionValue) == normalizedInput }
            ?: (expressions + coreExpressions()).firstOrNull { SearchNormalizer.normalize(it.expressionValue) == normalizedInput }

    private fun TermSearchDocument.coreExpressions() = listOf(
        TermExpression(0, term.termId, ExpressionType.Korean, term.koreanName, true),
        TermExpression(0, term.termId, ExpressionType.English, term.englishName, true),
        TermExpression(0, term.termId, ExpressionType.DB_COLUMN, term.englishAbbreviation, true),
    )

    private fun TermSearchDocument.statusIssue(request: RuleValidationRequest): ValidationIssue? = when (term.status) {
        TermStatus.Approved -> null
        TermStatus.Draft -> request.issue(
            severity = if (request.developmentUsage) ValidationSeverity.ERROR else ValidationSeverity.WARNING,
            standardTerm = term.koreanName,
            recommendedExpression = recommendedExpression(request.expressionType),
            reason = "Draft 용어는 개발 산출물에서 사용할 수 없습니다. 데이터 표준 승인 후 사용해야 합니다.",
        )
        TermStatus.Reviewing -> request.issue(
            severity = if (request.developmentUsage) ValidationSeverity.ERROR else ValidationSeverity.WARNING,
            standardTerm = term.koreanName,
            recommendedExpression = recommendedExpression(request.expressionType),
            reason = "Reviewing 용어는 개발 산출물에서 사용할 수 없습니다. 검토 완료 후 사용해야 합니다.",
        )
        TermStatus.Deprecated -> request.issue(
            severity = ValidationSeverity.ERROR,
            standardTerm = term.koreanName,
            reason = "Deprecated 용어는 사용할 수 없습니다. 대체 표준 용어를 확인해야 합니다.",
        )
        TermStatus.Rejected -> request.issue(
            severity = ValidationSeverity.ERROR,
            reason = "Rejected 용어는 추천하거나 사용할 수 없습니다.",
        )
    }

    private fun TermSearchDocument.recommendedExpression(expressionType: ExpressionType): String = when (expressionType) {
        ExpressionType.Korean, ExpressionType.UI_LABEL, ExpressionType.TEST_WORD -> term.koreanName
        ExpressionType.English -> term.englishName
        ExpressionType.DB_COLUMN -> term.englishAbbreviation
        ExpressionType.API_FIELD -> apiField()
        ExpressionType.CODE_VARIABLE -> codeVariable()
    }

    private fun RuleValidationRequest.issue(
        severity: ValidationSeverity,
        reason: String,
        standardTerm: String? = null,
        recommendedExpression: String? = null,
    ): ValidationIssue = ValidationIssue(
        severity = severity,
        source = source,
        inputExpression = inputExpression,
        reason = reason,
        location = location,
        standardTerm = standardTerm,
        recommendedExpression = recommendedExpression,
    )

    private fun PhysicalSpecValidationRequest.issue(
        document: TermSearchDocument,
        fieldName: String,
        expected: String,
        actual: String,
    ): ValidationIssue = ValidationIssue(
        severity = ValidationSeverity.ERROR,
        source = source,
        inputExpression = termId,
        location = location,
        standardTerm = document.term.koreanName,
        recommendedExpression = expected,
        reason = "${document.term.koreanName}의 ${fieldName} 표준값은 ${expected}이나 입력값은 ${actual}입니다.",
    )
}

data class RuleValidationRequest(
    val source: String,
    val inputExpression: String,
    val expressionType: ExpressionType,
    val location: String? = null,
    val developmentUsage: Boolean = true,
)

data class PhysicalSpecValidationRequest(
    val source: String,
    val termId: String,
    val physicalType: String,
    val digits: Int,
    val decimalPoint: Int,
    val location: String? = null,
)
