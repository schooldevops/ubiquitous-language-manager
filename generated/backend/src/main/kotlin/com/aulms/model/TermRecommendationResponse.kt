package com.aulms.model

import java.util.Objects
import com.aulms.model.GraphRecommendationContext
import com.aulms.model.RecommendationEvidence
import com.aulms.model.RecommendedTermDraft
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
 * RAG 조회, Graph 관계 확장, LLM 추론을 거쳐 생성한 용어 초안 추천 결과
 * @param inputKoreanName 
 * @param normalizedKoreanName 
 * @param recommendation 
 * @param ragMatches RAG/semantic 검색으로 찾은 근거 용어
 * @param graphContext 
 * @param llmReasoning 검색과 관계 문맥을 바탕으로 추천값을 조합한 추론 설명
 * @param warnings 
 */
data class TermRecommendationResponse(

    @Schema(example = "고객선호배송시간대", required = true, description = "")
    @get:JsonProperty("inputKoreanName", required = true) val inputKoreanName: kotlin.String,

    @Schema(example = "고객선호배송시간대", required = true, description = "")
    @get:JsonProperty("normalizedKoreanName", required = true) val normalizedKoreanName: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("recommendation", required = true) val recommendation: RecommendedTermDraft,

    @field:Valid
    @Schema(example = "null", required = true, description = "RAG/semantic 검색으로 찾은 근거 용어")
    @get:JsonProperty("ragMatches", required = true) val ragMatches: kotlin.collections.List<RecommendationEvidence>,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("graphContext", required = true) val graphContext: GraphRecommendationContext,

    @Schema(example = "고객 도메인 용어와 배송 시간대 의미를 결합해 Customer Preferred Delivery Time Slot 및 CUST_PREF_DLV_TM_SLOT 후보를 추천함", required = true, description = "검색과 관계 문맥을 바탕으로 추천값을 조합한 추론 설명")
    @get:JsonProperty("llmReasoning", required = true) val llmReasoning: kotlin.String,

    @Schema(example = "[\"데이터 사전에 동일한 Approved 용어는 없어 신규 후보로 유지해야 함\",\"배송 시간대가 코드 체계로 관리된다면 상태코드/구간코드와 구분해야 함\"]", required = true, description = "")
    @get:JsonProperty("warnings", required = true) val warnings: kotlin.collections.List<kotlin.String>
    ) {

}

