package com.aulms.model

import java.util.Objects
import com.aulms.model.ValidationSeverity
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
 * 표준 위반 경고
 * @param inputExpression 
 * @param severity 
 * @param reason 
 * @param standardTerm 
 * @param recommendedExpression 
 */
data class StandardViolationWarning(

    @Schema(example = "고객ID", required = true, description = "")
    @get:JsonProperty("inputExpression", required = true) val inputExpression: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("severity", required = true) val severity: ValidationSeverity,

    @Schema(example = "고객ID는 비표준 표현이며 고객번호 사용 권장", required = true, description = "")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @Schema(example = "고객번호", description = "")
    @get:JsonProperty("standardTerm") val standardTerm: kotlin.String? = null,

    @Schema(example = "customerNumber", description = "")
    @get:JsonProperty("recommendedExpression") val recommendedExpression: kotlin.String? = null
    ) {

}

