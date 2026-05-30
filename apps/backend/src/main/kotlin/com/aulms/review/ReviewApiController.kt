package com.aulms.review

import com.aulms.api.ReviewApi
import com.aulms.model.ArtifactValidationRequest
import com.aulms.model.DocumentReviewRequest
import com.aulms.model.PullRequestArtifactReviewRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewApiController(
    private val service: DocumentReviewService,
    private val artifactValidationService: ArtifactValidationService,
) : ReviewApi {
    override fun reviewDocument(documentReviewRequest: DocumentReviewRequest) =
        ResponseEntity.ok(service.review(documentReviewRequest))

    override fun reviewOpenApiArtifact(artifactValidationRequest: ArtifactValidationRequest) =
        ResponseEntity.ok(artifactValidationService.validateOpenApi(artifactValidationRequest))

    override fun reviewDdlArtifact(artifactValidationRequest: ArtifactValidationRequest) =
        ResponseEntity.ok(artifactValidationService.validateDdl(artifactValidationRequest))

    override fun reviewCodeArtifact(artifactValidationRequest: ArtifactValidationRequest) =
        ResponseEntity.ok(artifactValidationService.validateCode(artifactValidationRequest))

    override fun reviewPullRequestArtifacts(pullRequestArtifactReviewRequest: PullRequestArtifactReviewRequest) =
        ResponseEntity.ok(artifactValidationService.validatePullRequest(pullRequestArtifactReviewRequest))

    override fun validateArtifact(artifactValidationRequest: ArtifactValidationRequest) =
        ResponseEntity.ok(artifactValidationService.validate(artifactValidationRequest))
}
