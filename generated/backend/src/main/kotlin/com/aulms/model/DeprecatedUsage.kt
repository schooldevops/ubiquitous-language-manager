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
 * 폐기어 또는 금지어 사용 위치
 * @param expression 
 * @param aliasType 
 * @param termId 
 * @param standardTerm 
 * @param recommendedExpression 
 * @param reason 
 */
data class DeprecatedUsage(

    @Schema(example = "CUST_ID", required = true, description = "")
    @get:JsonProperty("expression", required = true) val expression: kotlin.String,

    @Schema(example = "Forbidden", required = true, description = "")
    @get:JsonProperty("aliasType", required = true) val aliasType: kotlin.String,

    @Schema(example = "T-000001", required = true, description = "")
    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @Schema(example = "고객번호", required = true, description = "")
    @get:JsonProperty("standardTerm", required = true) val standardTerm: kotlin.String,

    @Schema(example = "CUST_NO", required = true, description = "")
    @get:JsonProperty("recommendedExpression", required = true) val recommendedExpression: kotlin.String,

    @Schema(example = "기술 ID와 업무 고객번호가 혼동될 수 있음", required = true, description = "")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String
    ) {

}

