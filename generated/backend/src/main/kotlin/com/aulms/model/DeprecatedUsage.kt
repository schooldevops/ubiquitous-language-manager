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
 * 폐기어 또는 금지어 사용 위치
 * @param expression 
 * @param aliasType 
 * @param termId 
 * @param standardTerm 
 * @param recommendedExpression 
 * @param reason 
 */
data class DeprecatedUsage(

    @get:JsonProperty("expression", required = true) val expression: kotlin.String,

    @get:JsonProperty("aliasType", required = true) val aliasType: kotlin.String,

    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @get:JsonProperty("standardTerm", required = true) val standardTerm: kotlin.String,

    @get:JsonProperty("recommendedExpression", required = true) val recommendedExpression: kotlin.String,

    @get:JsonProperty("reason", required = true) val reason: kotlin.String
    ) {

}

