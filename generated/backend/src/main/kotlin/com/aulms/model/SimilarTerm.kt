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
 * 신규 후보와 유사한 기존 용어
 * @param termId 
 * @param koreanName 
 * @param englishName 
 * @param dbColumn 
 * @param apiField 
 * @param reason 
 */
data class SimilarTerm(

    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @get:JsonProperty("koreanName", required = true) val koreanName: kotlin.String,

    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @get:JsonProperty("dbColumn", required = true) val dbColumn: kotlin.String,

    @get:JsonProperty("apiField", required = true) val apiField: kotlin.String,

    @get:JsonProperty("reason", required = true) val reason: kotlin.String
    ) {

}

