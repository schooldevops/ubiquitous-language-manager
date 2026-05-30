package com.aulms.model

import java.util.Objects
import com.aulms.model.AssistTargetArtifact
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
 * 생성 산출물 placeholder
 * @param artifactType 
 * @param name 
 * @param content 표준 용어 기반으로 생성한 예시 내용
 */
data class GeneratedArtifact(

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("artifactType", required = true) val artifactType: AssistTargetArtifact,

    @Schema(example = "CustomerOrderResponse", required = true, description = "")
    @get:JsonProperty("name", required = true) val name: kotlin.String,

    @Schema(example = "null", required = true, description = "표준 용어 기반으로 생성한 예시 내용")
    @get:JsonProperty("content", required = true) val content: kotlin.String
    ) {

}

