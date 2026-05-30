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
 * 프롬프트에 주입할 신규 용어 후보
 * @param candidateTerm 
 * @param reason 
 * @param approvalRequired 
 * @param recommendedEnglishName 
 * @param recommendedAbbreviation 
 */
data class PromptCandidateTerm(

    @Schema(example = "고객선호배송시간대", required = true, description = "")
    @get:JsonProperty("candidateTerm", required = true) val candidateTerm: kotlin.String,

    @Schema(example = "데이터 사전에 일치하는 승인 용어가 없음", required = true, description = "")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @Schema(example = "true", required = true, description = "")
    @get:JsonProperty("approvalRequired", required = true) val approvalRequired: kotlin.Boolean,

    @Schema(example = "Customer Preferred Delivery Time Slot", description = "")
    @get:JsonProperty("recommendedEnglishName") val recommendedEnglishName: kotlin.String? = null,

    @Schema(example = "CUST_PREF_DLV_TM_SLOT", description = "")
    @get:JsonProperty("recommendedAbbreviation") val recommendedAbbreviation: kotlin.String? = null
    ) {

}

