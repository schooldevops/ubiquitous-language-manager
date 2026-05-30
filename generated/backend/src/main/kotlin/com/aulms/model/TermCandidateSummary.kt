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
import io.swagger.v3.oas.annotations.media.Schema

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

    @Schema(example = "CAND-000001", required = true, description = "")
    @get:JsonProperty("candidateId", required = true) val candidateId: kotlin.String,

    @Schema(example = "고객선호배송시간대", required = true, description = "")
    @get:JsonProperty("koreanName", required = true) val koreanName: kotlin.String,

    @Schema(example = "Customer Preferred Delivery Time Slot", required = true, description = "")
    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @Schema(example = "CUST_PREF_DLV_TM_SLOT", required = true, description = "")
    @get:JsonProperty("englishAbbreviation", required = true) val englishAbbreviation: kotlin.String,

    @Schema(example = "고객", required = true, description = "")
    @get:JsonProperty("domainName", required = true) val domainName: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("status", required = true) val status: CandidateStatus,

    @Schema(example = "planner01", required = true, description = "")
    @get:JsonProperty("requestedBy", required = true) val requestedBy: kotlin.String,

    @Schema(example = "T-000018", description = "")
    @get:JsonProperty("promotedTermId") val promotedTermId: kotlin.String? = null
    ) {

}

