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
 * 프롬프트에 주입할 신규 용어 후보
 * @param candidateTerm 
 * @param reason 
 * @param approvalRequired 
 * @param recommendedEnglishName 
 * @param recommendedAbbreviation 
 */
data class PromptCandidateTerm(

    @get:JsonProperty("candidateTerm", required = true) val candidateTerm: kotlin.String,

    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @get:JsonProperty("approvalRequired", required = true) val approvalRequired: kotlin.Boolean,

    @get:JsonProperty("recommendedEnglishName") val recommendedEnglishName: kotlin.String? = null,

    @get:JsonProperty("recommendedAbbreviation") val recommendedAbbreviation: kotlin.String? = null
    ) {

}

