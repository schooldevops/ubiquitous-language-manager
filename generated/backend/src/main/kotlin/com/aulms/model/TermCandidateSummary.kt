package com.aulms.model

import java.util.Objects
import com.aulms.model.CandidateStatus
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
 * 신규 용어 후보 목록 요약
 * @param candidateId 
 * @param koreanName 
 * @param englishName 
 * @param englishAbbreviation 
 * @param domainName 
 * @param status 
 * @param requestedBy 
 * @param promotedTermId 
 */
data class TermCandidateSummary(

    @get:JsonProperty("candidateId", required = true) val candidateId: kotlin.String,

    @get:JsonProperty("koreanName", required = true) val koreanName: kotlin.String,

    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @get:JsonProperty("englishAbbreviation", required = true) val englishAbbreviation: kotlin.String,

    @get:JsonProperty("domainName", required = true) val domainName: kotlin.String,

    @field:Valid
    @get:JsonProperty("status", required = true) val status: CandidateStatus,

    @get:JsonProperty("requestedBy", required = true) val requestedBy: kotlin.String,

    @get:JsonProperty("promotedTermId") val promotedTermId: kotlin.String? = null
    ) {

}

