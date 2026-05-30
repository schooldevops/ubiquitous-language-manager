package com.aulms.model

import java.util.Objects
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
 * 프롬프트 템플릿 변경 이력
 * @param historyId 
 * @param templateId 
 * @param version 
 * @param changeType 
 * @param reason 
 * @param actor 
 * @param createdAt 
 */
data class PromptTemplateHistory(

    @Schema(example = "PT-HIST-000001", required = true, description = "")
    @get:JsonProperty("historyId", required = true) val historyId: kotlin.String,

    @Schema(example = "PT-VIBE-001", required = true, description = "")
    @get:JsonProperty("templateId", required = true) val templateId: kotlin.String,

    @Schema(example = "1.0.0", required = true, description = "")
    @get:JsonProperty("version", required = true) val version: kotlin.String,

    @Schema(example = "Created", required = true, description = "")
    @get:JsonProperty("changeType", required = true) val changeType: PromptTemplateHistory.ChangeType,

    @Schema(example = "MVP3 기본 템플릿 등록", required = true, description = "")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @Schema(example = "system", required = true, description = "")
    @get:JsonProperty("actor", required = true) val actor: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("createdAt", required = true) val createdAt: java.time.OffsetDateTime
    ) {

    /**
    * 
    * Values: Created,Updated,Deprecated
    */
    enum class ChangeType(@get:JsonValue val value: kotlin.String) {

        Created("Created"),
        Updated("Updated"),
        Deprecated("Deprecated");

        companion object {
            @JvmStatic
            @JsonCreator
            fun forValue(value: kotlin.String): ChangeType {
                return values().first{it -> it.value == value}
            }
        }
    }

}

