package com.aulms.model

import java.util.Objects
import com.aulms.model.ArtifactValidationResult
import com.aulms.model.ArtifactValidationSummary
import com.aulms.model.ValidationIssue
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.Valid

/**
 * PR 변경 파일 표준 용어 검증 결과
 * @param pullRequestId PR 번호 또는 외부 시스템 PR 식별자
 * @param results 파일별 검증 결과
 * @param summary 
 * @param issues 전체 파일의 표준 위반 또는 권고 사항
 * @param exitCode CLI/CI 호환 종료 코드
 * @param repository 저장소 이름
 */
data class PullRequestArtifactReviewResult(

    @get:JsonProperty("pullRequestId", required = true) val pullRequestId: kotlin.String,

    @field:Valid
    @get:JsonProperty("results", required = true) val results: kotlin.collections.List<ArtifactValidationResult>,

    @field:Valid
    @get:JsonProperty("summary", required = true) val summary: ArtifactValidationSummary,

    @field:Valid
    @get:JsonProperty("issues", required = true) val issues: kotlin.collections.List<ValidationIssue>,

    @get:JsonProperty("exitCode", required = true) val exitCode: kotlin.Int,

    @get:JsonProperty("repository") val repository: kotlin.String? = null
    ) {

}

