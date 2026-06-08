package com.aulms.term

import com.aulms.model.TermAlias
import com.aulms.model.TermAliasCreateRequest
import com.aulms.model.TermApprovalRequest
import com.aulms.model.TermChangeHistoryListResponse
import com.aulms.model.TermCreateRequest
import com.aulms.model.TermDeprecationRequest
import com.aulms.model.TermExpression
import com.aulms.model.TermExpressionCreateRequest
import com.aulms.model.TermListResponse
import com.aulms.model.TermStatus
import com.aulms.model.TermUpdateRequest
import org.springframework.stereotype.Service

@Service
class TermService(private val repository: TermRepository) {
    fun listTerms(q: String?, domainName: String?, status: TermStatus?, page: Int, size: Int): TermListResponse =
        repository.list(q, domainName, status, page, size)

    fun getTerm(termId: String) = repository.get(termId)

    fun createTerm(request: TermCreateRequest) = repository.create(request.toCommand())

    fun updateTerm(termId: String, request: TermUpdateRequest) = repository.update(
        termId = termId,
        command = request.toCommand(),
        version = request.version,
        reason = request.changeReason,
    )

    fun approveTerm(termId: String, request: TermApprovalRequest) =
        repository.approve(termId, request.approver, request.reason)

    fun deprecateTerm(termId: String, request: TermDeprecationRequest) =
        repository.deprecate(termId, request.approver, request.replacementTermId, request.reason, request.impactAnalysisId)

    fun listHistory(termId: String, page: Int, size: Int): TermChangeHistoryListResponse {
        val (items, metadata) = repository.listHistory(termId, page, size)
        return TermChangeHistoryListResponse(items = items, page = metadata)
    }

    fun listExpressions(termId: String): List<TermExpression> = repository.listExpressions(termId)

    fun createExpression(termId: String, request: TermExpressionCreateRequest): TermExpression =
        repository.addExpression(termId, request.expressionType, request.expressionValue, request.language, request.style, request.isStandard)

    fun replaceExpressions(termId: String, requests: List<TermExpressionCreateRequest>): List<TermExpression> {
        repository.deleteExpressions(termId)
        return requests.map {
            repository.addExpression(termId, it.expressionType, it.expressionValue, it.language, it.style, it.isStandard)
        }
    }

    fun listAliases(termId: String): List<TermAlias> = repository.listAliases(termId)

    fun createAlias(termId: String, request: TermAliasCreateRequest): TermAlias =
        repository.addAlias(termId, request.aliasName, request.aliasType, request.recommendationAction, request.reason)

    fun replaceAliases(termId: String, requests: List<TermAliasCreateRequest>): List<TermAlias> {
        repository.deleteAliases(termId)
        return requests.map {
            repository.addAlias(termId, it.aliasName, it.aliasType, it.recommendationAction, it.reason)
        }
    }
}

private fun TermCreateRequest.toCommand(): TermCommand = TermCommand(
    domainName = domainName,
    usageType = usageType,
    koreanName = koreanName,
    englishName = englishName,
    englishAbbreviation = englishAbbreviation,
    businessDefinition = businessDefinition,
    usageContext = usageContext,
    physicalType = physicalType,
    digits = digits,
    decimalPoint = decimalPoint,
    owner = owner,
    status = status ?: TermStatus.Draft,
)

private fun TermUpdateRequest.toCommand(): TermCommand = TermCommand(
    domainName = domainName,
    usageType = usageType,
    koreanName = koreanName,
    englishName = englishName,
    englishAbbreviation = englishAbbreviation,
    businessDefinition = businessDefinition,
    usageContext = usageContext,
    physicalType = physicalType,
    digits = digits,
    decimalPoint = decimalPoint,
    owner = owner,
    status = status ?: TermStatus.Draft,
)

