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
 * 요구사항에서 추출한 업무 개념
 * @param concept 
 * @param reason 
 * @param confidence 
 */
data class ExtractedBusinessConcept(

    @get:JsonProperty("concept", required = true) val concept: kotlin.String,

    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @get:DecimalMin("0")
    @get:DecimalMax("1")
    @get:JsonProperty("confidence", required = true) val confidence: kotlin.Double
    ) {

}

