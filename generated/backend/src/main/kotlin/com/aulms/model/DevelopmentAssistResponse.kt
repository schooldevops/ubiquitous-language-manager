package com.aulms.model

import java.util.Objects
import com.aulms.model.AssistCandidateTerm
import com.aulms.model.AssistTermMapping
import com.aulms.model.ExtractedBusinessConcept
import com.aulms.model.GeneratedArtifact
import com.aulms.model.StandardViolationWarning
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
import io.swagger.v3.oas.annotations.media.Schema

/**
 * AI 산출물 생성 지원 응답
 * @param requirementText 
 * @param extractedConcepts 요구사항에서 추출한 업무 개념 후보
 * @param termMappings 데이터 사전 기준 표준 용어 매핑
 * @param generatedArtifacts 표준 용어 매핑으로 생성한 산출물 예시
 * @param candidateTerms 데이터 사전에 없어 승인 전 후보로 분리한 신규 용어
 * @param warnings Rule Engine과 표준 정책 기반 위반 경고
 */
data class DevelopmentAssistResponse(

    @Schema(example = "고객별 주문 내역을 조회하는 API 만들어줘.", required = true, description = "")
    @get:JsonProperty("requirementText", required = true) val requirementText: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "요구사항에서 추출한 업무 개념 후보")
    @get:JsonProperty("extractedConcepts", required = true) val extractedConcepts: kotlin.collections.List<ExtractedBusinessConcept>,

    @field:Valid
    @Schema(example = "null", required = true, description = "데이터 사전 기준 표준 용어 매핑")
    @get:JsonProperty("termMappings", required = true) val termMappings: kotlin.collections.List<AssistTermMapping>,

    @field:Valid
    @Schema(example = "null", required = true, description = "표준 용어 매핑으로 생성한 산출물 예시")
    @get:JsonProperty("generatedArtifacts", required = true) val generatedArtifacts: kotlin.collections.List<GeneratedArtifact>,

    @field:Valid
    @Schema(example = "null", required = true, description = "데이터 사전에 없어 승인 전 후보로 분리한 신규 용어")
    @get:JsonProperty("candidateTerms", required = true) val candidateTerms: kotlin.collections.List<AssistCandidateTerm>,

    @field:Valid
    @Schema(example = "null", required = true, description = "Rule Engine과 표준 정책 기반 위반 경고")
    @get:JsonProperty("warnings", required = true) val warnings: kotlin.collections.List<StandardViolationWarning>
    ) {

}

