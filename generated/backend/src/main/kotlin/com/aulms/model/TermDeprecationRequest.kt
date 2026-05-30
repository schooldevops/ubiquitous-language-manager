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
 * 용어 폐기 요청
 * @param approver 
 * @param replacementTermId 
 * @param reason 
 * @param impactAnalysisId 
 */
data class TermDeprecationRequest(

    @get:Size(min=1)
    @Schema(example = "data.steward", required = true, description = "")
    @get:JsonProperty("approver", required = true) val approver: kotlin.String,

    @get:Size(min=1)
    @Schema(example = "T-000001", required = true, description = "")
    @get:JsonProperty("replacementTermId", required = true) val replacementTermId: kotlin.String,

    @get:Size(min=1)
    @Schema(example = "CUST_ID는 기술 ID와 혼동 가능하여 고객번호로 대체", required = true, description = "")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @Schema(example = "IA-000001", description = "")
    @get:JsonProperty("impactAnalysisId") val impactAnalysisId: kotlin.String? = null
    ) {

}

