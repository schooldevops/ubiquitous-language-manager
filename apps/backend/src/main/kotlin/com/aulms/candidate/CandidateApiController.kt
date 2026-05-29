package com.aulms.candidate

import com.aulms.api.CandidateApi
import com.aulms.model.CandidateStatus
import com.aulms.model.TermCandidateCreateRequest
import com.aulms.model.TermCandidatePromoteRequest
import com.aulms.model.TermCandidateReviewRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class CandidateApiController(private val service: CandidateService) : CandidateApi {
    override fun createCandidate(termCandidateCreateRequest: TermCandidateCreateRequest) =
        ResponseEntity.status(HttpStatus.CREATED).body(service.createCandidate(termCandidateCreateRequest))

    override fun listCandidates(q: String?, status: CandidateStatus?, domainName: String?, page: Int, size: Int) =
        ResponseEntity.ok(service.listCandidates(q, status, domainName, page, size))

    override fun getCandidate(candidateId: String) =
        ResponseEntity.ok(service.getCandidate(candidateId))

    override fun reviewCandidate(candidateId: String, termCandidateReviewRequest: TermCandidateReviewRequest) =
        ResponseEntity.ok(service.reviewCandidate(candidateId, termCandidateReviewRequest))

    override fun promoteCandidate(candidateId: String, termCandidatePromoteRequest: TermCandidatePromoteRequest) =
        ResponseEntity.ok(service.promoteCandidate(candidateId, termCandidatePromoteRequest))
}
