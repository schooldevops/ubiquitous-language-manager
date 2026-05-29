package com.aulms.model

import java.util.Objects
import com.aulms.model.PromptCandidateTerm
import com.aulms.model.PromptTermMapping
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
 * 프롬프트 템플릿 미리보기 요청
 * @param requirementText 사용자가 입력한 요구사항 또는 기획서 초안
 * @param termMappings 데이터 사전 검색으로 확정한 표준 용어 매핑 목록
 * @param candidateTerms 데이터 사전에 없어 후보로 분리한 신규 용어 목록
 * @param additionalContext AI 도구에 함께 전달할 추가 맥락
 */
data class PromptTemplatePreviewRequest(

    @get:Size(min=1)
    @get:JsonProperty("requirementText", required = true) val requirementText: kotlin.String,

    @field:Valid
    @get:JsonProperty("termMappings") val termMappings: kotlin.collections.List<PromptTermMapping>? = null,

    @field:Valid
    @get:JsonProperty("candidateTerms") val candidateTerms: kotlin.collections.List<PromptCandidateTerm>? = null,

    @get:JsonProperty("additionalContext") val additionalContext: kotlin.String? = null
    ) {

}

