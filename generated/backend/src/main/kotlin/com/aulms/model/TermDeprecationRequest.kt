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
 * 용어 폐기 요청
 * @param approver 
 * @param replacementTermId 
 * @param reason 
 * @param impactAnalysisId 
 */
data class TermDeprecationRequest(

    @get:Size(min=1)
    @get:JsonProperty("approver", required = true) val approver: kotlin.String,

    @get:Size(min=1)
    @get:JsonProperty("replacementTermId", required = true) val replacementTermId: kotlin.String,

    @get:Size(min=1)
    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @get:JsonProperty("impactAnalysisId") val impactAnalysisId: kotlin.String? = null
    ) {

}

