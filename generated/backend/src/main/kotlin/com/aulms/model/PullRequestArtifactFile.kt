package com.aulms.model

import java.util.Objects
import com.aulms.model.ArtifactSourceType
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
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
 * PR 검증 대상 파일
 * @param filePath 변경 파일 경로
 * @param content 변경 파일 본문
 * @param sourceType 
 */
data class PullRequestArtifactFile(

    @Schema(example = "apps/backend/src/main/kotlin/CustomerOrderResponse.kt", required = true, description = "변경 파일 경로")
    @get:JsonProperty("filePath", required = true) val filePath: kotlin.String,

    @get:Size(min=1)
    @Schema(example = "null", required = true, description = "변경 파일 본문")
    @get:JsonProperty("content", required = true) val content: kotlin.String,

    @field:Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("sourceType") val sourceType: ArtifactSourceType? = null
    ) {

}

