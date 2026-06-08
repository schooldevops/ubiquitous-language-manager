package com.aulms.term

import com.aulms.api.AliasApi
import com.aulms.api.ExpressionApi
import com.aulms.api.GovernanceApi
import com.aulms.api.TermApi
import com.aulms.model.TermAliasCreateRequest
import com.aulms.model.TermApprovalRequest
import com.aulms.model.TermCreateRequest
import com.aulms.model.TermDeprecationRequest
import com.aulms.model.TermExpressionCreateRequest
import com.aulms.model.TermStatus
import com.aulms.model.TermUpdateRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class TermApiController(private val service: TermService) : TermApi, ExpressionApi, AliasApi, GovernanceApi {
    override fun createTerm(termCreateRequest: TermCreateRequest) =
        ResponseEntity.status(HttpStatus.CREATED).body(service.createTerm(termCreateRequest))

    override fun listTerms(q: String?, domainName: String?, status: TermStatus?, page: Int, size: Int) =
        ResponseEntity.ok(service.listTerms(q, domainName, status, page, size))

    override fun getTerm(termId: String) =
        ResponseEntity.ok(service.getTerm(termId))

    override fun updateTerm(termId: String, termUpdateRequest: TermUpdateRequest) =
        ResponseEntity.ok(service.updateTerm(termId, termUpdateRequest))

    override fun approveTerm(termId: String, termApprovalRequest: TermApprovalRequest) =
        ResponseEntity.ok(service.approveTerm(termId, termApprovalRequest))

    override fun deprecateTerm(termId: String, termDeprecationRequest: TermDeprecationRequest) =
        ResponseEntity.ok(service.deprecateTerm(termId, termDeprecationRequest))

    override fun listTermHistory(termId: String, page: Int, size: Int) =
        ResponseEntity.ok(service.listHistory(termId, page, size))

    override fun listTermExpressions(termId: String) =
        ResponseEntity.ok(service.listExpressions(termId))

    override fun createTermExpression(termId: String, termExpressionCreateRequest: TermExpressionCreateRequest) =
        ResponseEntity.status(HttpStatus.CREATED).body(service.createExpression(termId, termExpressionCreateRequest))

    override fun replaceTermExpressions(termId: String, termExpressionCreateRequest: List<TermExpressionCreateRequest>) =
        ResponseEntity.ok(service.replaceExpressions(termId, termExpressionCreateRequest))

    override fun listTermAliases(termId: String) =
        ResponseEntity.ok(service.listAliases(termId))

    override fun createTermAlias(termId: String, termAliasCreateRequest: TermAliasCreateRequest) =
        ResponseEntity.status(HttpStatus.CREATED).body(service.createAlias(termId, termAliasCreateRequest))

    override fun replaceTermAliases(termId: String, termAliasCreateRequest: List<TermAliasCreateRequest>) =
        ResponseEntity.ok(service.replaceAliases(termId, termAliasCreateRequest))
}

