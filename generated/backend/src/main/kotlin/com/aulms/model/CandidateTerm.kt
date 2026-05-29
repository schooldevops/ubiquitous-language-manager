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
 * 데이터 사전에 없는 신규 용어 후보
 * @param expression 신규 후보 표현
 * @param reason 후보로 판단한 사유
 * @param approvalRequired 데이터 표준 승인 필요 여부
 * @param recommendedKoreanName 추천 한글 표준 용어명
 */
data class CandidateTerm(

    @get:JsonProperty("expression", required = true) val expression: kotlin.String,

    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @get:JsonProperty("approvalRequired", required = true) val approvalRequired: kotlin.Boolean,

    @get:JsonProperty("recommendedKoreanName") val recommendedKoreanName: kotlin.String? = null
    ) {

}

