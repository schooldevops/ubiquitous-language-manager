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

    @get:JsonProperty("templateId", required = true) val templateId: kotlin.String,

    @get:JsonProperty("version", required = true) val version: kotlin.String,

    @field:Valid
    @get:JsonProperty("status", required = true) val status: PromptTemplateStatus,

    @get:JsonProperty("changeReason", required = true) val changeReason: kotlin.String,

    @get:JsonProperty("createdBy", required = true) val createdBy: kotlin.String,

    @get:JsonProperty("createdAt", required = true) val createdAt: java.time.OffsetDateTime
    ) {

}

