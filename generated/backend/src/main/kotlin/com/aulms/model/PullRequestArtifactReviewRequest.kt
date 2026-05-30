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

/**
 * PR 변경 파일 표준 용어 검증 요청
 * @param pullRequestId PR 번호 또는 외부 시스템 PR 식별자
 * @param files 검증할 변경 파일 목록
 * @param repository 저장소 이름
 * @param failOnWarning WARNING도 실패 exitCode로 볼지 여부
 * @param includeSuggestions 권장 표현 포함 여부
 */
data class PullRequestArtifactReviewRequest(

    @get:JsonProperty("pullRequestId", required = true) val pullRequestId: kotlin.String,

    @field:Valid
    @get:Size(min=1)
    @get:JsonProperty("files", required = true) val files: kotlin.collections.List<PullRequestArtifactFile>,

    @get:JsonProperty("repository") val repository: kotlin.String? = null,

    @get:JsonProperty("failOnWarning") val failOnWarning: kotlin.Boolean? = false,

    @get:JsonProperty("includeSuggestions") val includeSuggestions: kotlin.Boolean? = true
    ) {

}

