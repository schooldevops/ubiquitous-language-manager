package com.aulms.model

import java.util.Objects
import com.aulms.model.PromptTemplateHistory
import com.aulms.model.PromptTemplateStatus
import com.aulms.model.PromptTemplateType
import com.aulms.model.PromptTemplateVariable
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
 * 프롬프트 템플릿 상세 정보
 * @param templateId 
 * @param type 
 * @param name 
 * @param version SemVer 형식의 템플릿 버전
 * @param status 
 * @param description 
 * @param body Mustache 스타일 변수 플레이스홀더를 포함한 템플릿 본문
 * @param variables 템플릿에 주입 가능한 변수 목록
 * @param versionPolicy 템플릿 버전 변경 정책
 * @param histories 템플릿 변경 이력
 * @param createdAt 
 * @param updatedAt 
 */
data class PromptTemplate(

    @get:JsonProperty("templateId", required = true) val templateId: kotlin.String,

    @field:Valid
    @get:JsonProperty("type", required = true) val type: PromptTemplateType,

    @get:JsonProperty("name", required = true) val name: kotlin.String,

    @get:JsonProperty("version", required = true) val version: kotlin.String,

    @field:Valid
    @get:JsonProperty("status", required = true) val status: PromptTemplateStatus,

    @get:JsonProperty("description", required = true) val description: kotlin.String,

    @get:JsonProperty("body", required = true) val body: kotlin.String,

    @field:Valid
    @get:JsonProperty("variables", required = true) val variables: kotlin.collections.List<PromptTemplateVariable>,

    @get:JsonProperty("versionPolicy", required = true) val versionPolicy: kotlin.String,

    @field:Valid
    @get:JsonProperty("histories", required = true) val histories: kotlin.collections.List<PromptTemplateHistory>,

    @get:JsonProperty("createdAt", required = true) val createdAt: java.time.OffsetDateTime,

    @get:JsonProperty("updatedAt", required = true) val updatedAt: java.time.OffsetDateTime
    ) {

}

