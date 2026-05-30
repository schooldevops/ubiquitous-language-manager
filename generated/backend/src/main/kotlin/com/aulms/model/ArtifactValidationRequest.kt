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

/**
 * 개발 산출물 표준 용어 검증 요청
 * @param sourceType 
 * @param filePath 검증 대상 파일 경로
 * @param content 검증 대상 파일 본문
 * @param domainNames 검증 대상 도메인 필터
 * @param failOnWarning WARNING도 실패 exitCode로 볼지 여부
 * @param includeSuggestions 권장 표현을 검증 결과에 포함할지 여부
 */
data class ArtifactValidationRequest(

    @field:Valid
    @get:JsonProperty("sourceType", required = true) val sourceType: ArtifactSourceType,

    @get:JsonProperty("filePath", required = true) val filePath: kotlin.String,

    @get:Size(min=1)
    @get:JsonProperty("content", required = true) val content: kotlin.String,

    @get:JsonProperty("domainNames") val domainNames: kotlin.collections.List<kotlin.String>? = null,

    @get:JsonProperty("failOnWarning") val failOnWarning: kotlin.Boolean? = false,

    @get:JsonProperty("includeSuggestions") val includeSuggestions: kotlin.Boolean? = true
    ) {

}

