package com.aulms.model

import java.util.Objects
import com.aulms.model.PullRequestArtifactFile
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
import io.swagger.v3.oas.annotations.media.Schema

/**
 * PR 변경 파일 표준 용어 검증 요청
 * @param pullRequestId PR 번호 또는 외부 시스템 PR 식별자
 * @param files 검증할 변경 파일 목록
 * @param repository 저장소 이름
 * @param failOnWarning WARNING도 실패 exitCode로 볼지 여부
 * @param includeSuggestions 권장 표현 포함 여부
 */
data class PullRequestArtifactReviewRequest(

    @Schema(example = "123", required = true, description = "PR 번호 또는 외부 시스템 PR 식별자")
    @get:JsonProperty("pullRequestId", required = true) val pullRequestId: kotlin.String,

    @field:Valid
    @get:Size(min=1)
    @Schema(example = "null", required = true, description = "검증할 변경 파일 목록")
    @get:JsonProperty("files", required = true) val files: kotlin.collections.List<PullRequestArtifactFile>,

    @Schema(example = "aulms", description = "저장소 이름")
    @get:JsonProperty("repository") val repository: kotlin.String? = null,

    @Schema(example = "false", description = "WARNING도 실패 exitCode로 볼지 여부")
    @get:JsonProperty("failOnWarning") val failOnWarning: kotlin.Boolean? = false,

    @Schema(example = "true", description = "권장 표현 포함 여부")
    @get:JsonProperty("includeSuggestions") val includeSuggestions: kotlin.Boolean? = true
    ) {

}

