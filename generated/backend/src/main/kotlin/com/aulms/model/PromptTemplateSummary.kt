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

    @get:JsonProperty("templateId", required = true) val templateId: kotlin.String,

    @field:Valid
    @get:JsonProperty("type", required = true) val type: PromptTemplateType,

    @get:JsonProperty("name", required = true) val name: kotlin.String,

    @get:JsonProperty("version", required = true) val version: kotlin.String,

    @field:Valid
    @get:JsonProperty("status", required = true) val status: PromptTemplateStatus,

    @get:JsonProperty("description", required = true) val description: kotlin.String,

    @get:JsonProperty("updatedAt", required = true) val updatedAt: java.time.OffsetDateTime
    ) {

}

