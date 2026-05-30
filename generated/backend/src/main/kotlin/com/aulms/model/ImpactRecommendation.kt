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
 * 영향도 분석 권장 조치
 * @param priority 
 * @param action 
 * @param reason 
 */
data class ImpactRecommendation(

    @get:Min(1)
    @get:JsonProperty("priority", required = true) val priority: kotlin.Int,

    @get:JsonProperty("action", required = true) val action: kotlin.String,

    @get:JsonProperty("reason", required = true) val reason: kotlin.String
    ) {

}

