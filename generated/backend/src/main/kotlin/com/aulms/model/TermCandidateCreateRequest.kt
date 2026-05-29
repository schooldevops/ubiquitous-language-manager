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
 * 신규 용어 후보 등록 요청
 * @param koreanName 
 * @param englishName 
 * @param englishAbbreviation 
 * @param domainName 
 * @param businessDefinition 
 * @param physicalType 
 * @param digits 
 * @param decimalPoint 
 * @param requestedBy 
 * @param usageContext 
 */
data class TermCandidateCreateRequest(

    @get:Size(min=1)
    @get:JsonProperty("koreanName", required = true) val koreanName: kotlin.String,

    @get:Size(min=1)
    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @get:Pattern(regexp="^[A-Z][A-Z0-9_]*$")
    @get:Size(min=1)
    @get:JsonProperty("englishAbbreviation", required = true) val englishAbbreviation: kotlin.String,

    @get:Size(min=1)
    @get:JsonProperty("domainName", required = true) val domainName: kotlin.String,

    @get:Size(min=1)
    @get:JsonProperty("businessDefinition", required = true) val businessDefinition: kotlin.String,

    @get:Size(min=1)
    @get:JsonProperty("physicalType", required = true) val physicalType: kotlin.String,

    @get:Min(0)
    @get:JsonProperty("digits", required = true) val digits: kotlin.Int,

    @get:Min(0)
    @get:JsonProperty("decimalPoint", required = true) val decimalPoint: kotlin.Int,

    @get:Size(min=1)
    @get:JsonProperty("requestedBy", required = true) val requestedBy: kotlin.String,

    @get:JsonProperty("usageContext") val usageContext: kotlin.String? = null
    ) {

}

