package com.aulms.model

import java.util.Objects
import com.aulms.model.TermRecommendationMode
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
 * 한글 용어명을 기준으로 RAG, Graph, 추론 단계를 거쳐 용어 등록 초안을 추천하는 요청
 * @param koreanName 추천 기준이 되는 한글 용어명
 * @param mode 
 * @param currentDomainName 화면에서 이미 선택된 도메인
 * @param currentBusinessDefinition 사용자가 일부 입력한 업무 정의
 * @param currentUsageContext 사용자가 일부 입력한 사용 맥락
 */
data class TermRecommendationRequest(

    @get:Size(min=1)
    @Schema(example = "고객선호배송시간대", required = true, description = "추천 기준이 되는 한글 용어명")
    @get:JsonProperty("koreanName", required = true) val koreanName: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("mode", required = true) val mode: TermRecommendationMode,

    @Schema(example = "고객", description = "화면에서 이미 선택된 도메인")
    @get:JsonProperty("currentDomainName") val currentDomainName: kotlin.String? = null,

    @Schema(example = "고객이 선호하는 배송 시간대를 저장하기 위한 항목", description = "사용자가 일부 입력한 업무 정의")
    @get:JsonProperty("currentBusinessDefinition") val currentBusinessDefinition: kotlin.String? = null,

    @Schema(example = "배송 요청 화면과 배송 옵션 추천 API에서 사용", description = "사용자가 일부 입력한 사용 맥락")
    @get:JsonProperty("currentUsageContext") val currentUsageContext: kotlin.String? = null
    ) {

}

