package com.aulms.review

import com.aulms.api.ReviewApi
import com.aulms.model.DocumentReviewRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewApiController(private val service: DocumentReviewService) : ReviewApi {
    override fun reviewDocument(documentReviewRequest: DocumentReviewRequest) =
        ResponseEntity.ok(service.review(documentReviewRequest))
}
