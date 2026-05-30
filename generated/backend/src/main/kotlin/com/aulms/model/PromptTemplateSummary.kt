package com.aulms.model

import java.util.Objects
import com.aulms.model.PromptTemplateStatus
import com.aulms.model.PromptTemplateType
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
 * 프롬프트 템플릿 목록 요약
 * @param templateId 
 * @param type 
 * @param name 
 * @param version 
 * @param status 
 * @param description 
 * @param updatedAt 
 */
data class PromptTemplateSummary(

    @Schema(example = "PT-VIBE-001", required = true, description = "")
    @get:JsonProperty("templateId", required = true) val templateId: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("type", required = true) val type: PromptTemplateType,

    @Schema(example = "사내 데이터 사전 기반 개발 규칙", required = true, description = "")
    @get:JsonProperty("name", required = true) val name: kotlin.String,

    @Schema(example = "1.0.0", required = true, description = "")
    @get:JsonProperty("version", required = true) val version: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("status", required = true) val status: PromptTemplateStatus,

    @Schema(example = "DB, API, DTO, Entity, 테스트 생성 시 표준 용어를 강제하는 프롬프트", required = true, description = "")
    @get:JsonProperty("description", required = true) val description: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("updatedAt", required = true) val updatedAt: java.time.OffsetDateTime
    ) {

}

