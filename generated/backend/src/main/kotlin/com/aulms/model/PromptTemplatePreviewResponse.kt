package com.aulms.model

import java.util.Objects
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
 * 프롬프트 템플릿 미리보기 응답
 * @param templateId 
 * @param version 
 * @param renderedPrompt 변수 주입이 완료된 최종 프롬프트
 * @param injectedVariables 실제 주입된 변수명 목록
 */
data class PromptTemplatePreviewResponse(

    @get:JsonProperty("templateId", required = true) val templateId: kotlin.String,

    @get:JsonProperty("version", required = true) val version: kotlin.String,

    @get:JsonProperty("renderedPrompt", required = true) val renderedPrompt: kotlin.String,

    @get:JsonProperty("injectedVariables", required = true) val injectedVariables: kotlin.collections.List<kotlin.String>
    ) {

}

