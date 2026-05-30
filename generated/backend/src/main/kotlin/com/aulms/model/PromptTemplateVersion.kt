package com.aulms.model

import java.util.Objects
import com.aulms.model.PromptTemplateStatus
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
 * 프롬프트 템플릿 버전 정보
 * @param templateId 
 * @param version 
 * @param status 
 * @param changeReason 
 * @param createdBy 
 * @param createdAt 
 */
data class PromptTemplateVersion(

    @Schema(example = "PT-VIBE-001", required = true, description = "")
    @get:JsonProperty("templateId", required = true) val templateId: kotlin.String,

    @Schema(example = "1.0.0", required = true, description = "")
    @get:JsonProperty("version", required = true) val version: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("status", required = true) val status: PromptTemplateStatus,

    @Schema(example = "MVP3 기본 바이브코딩 프롬프트 등록", required = true, description = "")
    @get:JsonProperty("changeReason", required = true) val changeReason: kotlin.String,

    @Schema(example = "system", required = true, description = "")
    @get:JsonProperty("createdBy", required = true) val createdBy: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("createdAt", required = true) val createdAt: java.time.OffsetDateTime
    ) {

}

