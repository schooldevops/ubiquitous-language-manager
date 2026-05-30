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
 * 영향도 분석 권장 조치
 * @param priority 
 * @param action 
 * @param reason 
 */
data class ImpactRecommendation(

    @get:Min(1)
    @Schema(example = "1", required = true, description = "")
    @get:JsonProperty("priority", required = true) val priority: kotlin.Int,

    @Schema(example = "API v2에서 customerNumber 유지, customerId 입력은 deprecated 처리", required = true, description = "")
    @get:JsonProperty("action", required = true) val action: kotlin.String,

    @Schema(example = "API 필드명 변경은 외부 연동 영향이 큼", required = true, description = "")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String
    ) {

}

