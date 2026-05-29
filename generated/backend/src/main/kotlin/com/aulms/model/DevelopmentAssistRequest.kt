package com.aulms.model

import java.util.Objects
import com.aulms.model.AssistTargetArtifact
import com.fasterxml.jackson.annotation.JsonProperty
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
 * AI 산출물 생성 지원 요청
 * @param requirementText 사용자가 입력한 요구사항
 * @param targetArtifacts 생성할 산출물 유형 목록
 * @param domainNames 우선 검색할 업무 도메인 목록
 */
data class DevelopmentAssistRequest(

    @get:Size(min=1)
    @get:JsonProperty("requirementText", required = true) val requirementText: kotlin.String,

    @field:Valid
    @get:Size(min=1)
    @get:JsonProperty("targetArtifacts", required = true) val targetArtifacts: kotlin.collections.List<AssistTargetArtifact>,

    @get:JsonProperty("domainNames") val domainNames: kotlin.collections.List<kotlin.String>? = null
    ) {

}

