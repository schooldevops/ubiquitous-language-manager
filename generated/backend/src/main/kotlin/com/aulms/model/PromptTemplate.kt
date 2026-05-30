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
import io.swagger.v3.oas.annotations.media.Schema

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

    @Schema(example = "PT-VIBE-001", required = true, description = "")
    @get:JsonProperty("templateId", required = true) val templateId: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("type", required = true) val type: PromptTemplateType,

    @Schema(example = "사내 데이터 사전 기반 개발 규칙", required = true, description = "")
    @get:JsonProperty("name", required = true) val name: kotlin.String,

    @Schema(example = "1.0.0", required = true, description = "SemVer 형식의 템플릿 버전")
    @get:JsonProperty("version", required = true) val version: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("status", required = true) val status: PromptTemplateStatus,

    @Schema(example = "DB, API, DTO, Entity, 테스트 생성 시 표준 용어를 강제하는 프롬프트", required = true, description = "")
    @get:JsonProperty("description", required = true) val description: kotlin.String,

    @Schema(example = "# 사내 데이터 사전 기반 개발 규칙요구사항: {{requirementText}}", required = true, description = "Mustache 스타일 변수 플레이스홀더를 포함한 템플릿 본문")
    @get:JsonProperty("body", required = true) val body: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "템플릿에 주입 가능한 변수 목록")
    @get:JsonProperty("variables", required = true) val variables: kotlin.collections.List<PromptTemplateVariable>,

    @Schema(example = "MAJOR는 출력 구조 변경, MINOR는 규칙 추가, PATCH는 문구 보완에 사용한다.", required = true, description = "템플릿 버전 변경 정책")
    @get:JsonProperty("versionPolicy", required = true) val versionPolicy: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "템플릿 변경 이력")
    @get:JsonProperty("histories", required = true) val histories: kotlin.collections.List<PromptTemplateHistory>,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("createdAt", required = true) val createdAt: java.time.OffsetDateTime,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("updatedAt", required = true) val updatedAt: java.time.OffsetDateTime
    ) {

}

