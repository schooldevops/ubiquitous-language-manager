package com.aulms.model

import java.util.Objects
import com.aulms.model.TermStatus
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

/**
 * 
 * @param domainName 
 * @param usageType 
 * @param koreanName 
 * @param englishName 
 * @param englishAbbreviation 
 * @param businessDefinition 
 * @param physicalType 
 * @param digits 
 * @param decimalPoint 
 * @param owner 
 * @param version 
 * @param changeReason 
 * @param usageContext 
 * @param status 
 */
data class TermUpdateRequest(

    @get:Size(min=1,max=100)
    @get:JsonProperty("domainName", required = true) val domainName: kotlin.String,

    @get:Size(min=1,max=50)
    @get:JsonProperty("usageType", required = true) val usageType: kotlin.String,

    @get:Size(min=1,max=200)
    @get:JsonProperty("koreanName", required = true) val koreanName: kotlin.String,

    @get:Size(min=1,max=300)
    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @get:Pattern(regexp="^[A-Z][A-Z0-9_]*$")
    @get:Size(min=1,max=100)
    @get:JsonProperty("englishAbbreviation", required = true) val englishAbbreviation: kotlin.String,

    @get:Size(min=1)
    @get:JsonProperty("businessDefinition", required = true) val businessDefinition: kotlin.String,

    @get:Size(min=1,max=50)
    @get:JsonProperty("physicalType", required = true) val physicalType: kotlin.String,

    @get:Min(0)
    @get:JsonProperty("digits", required = true) val digits: kotlin.Int,

    @get:Min(0)
    @get:JsonProperty("decimalPoint", required = true) val decimalPoint: kotlin.Int,

    @get:Size(min=1,max=200)
    @get:JsonProperty("owner", required = true) val owner: kotlin.String,

    @get:Size(min=1)
    @get:JsonProperty("version", required = true) val version: kotlin.String,

    @get:Size(min=1)
    @get:JsonProperty("changeReason", required = true) val changeReason: kotlin.String,

    @get:JsonProperty("usageContext") val usageContext: kotlin.String? = null,

    @field:Valid
    @get:JsonProperty("status") val status: TermStatus? = null
    ) {

}

