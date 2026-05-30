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
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 데이터 사전에 없는 신규 용어 후보
 * @param expression 신규 후보 표현
 * @param reason 후보로 판단한 사유
 * @param approvalRequired 데이터 표준 승인 필요 여부
 * @param recommendedKoreanName 추천 한글 표준 용어명
 */
data class CandidateTerm(

    @Schema(example = "주문 리스트", required = true, description = "신규 후보 표현")
    @get:JsonProperty("expression", required = true) val expression: kotlin.String,

    @Schema(example = "데이터 사전에서 매핑되는 표준 용어를 찾지 못했습니다.", required = true, description = "후보로 판단한 사유")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @Schema(example = "true", required = true, description = "데이터 표준 승인 필요 여부")
    @get:JsonProperty("approvalRequired", required = true) val approvalRequired: kotlin.Boolean,

    @Schema(example = "주문목록", description = "추천 한글 표준 용어명")
    @get:JsonProperty("recommendedKoreanName") val recommendedKoreanName: kotlin.String? = null
    ) {

}

