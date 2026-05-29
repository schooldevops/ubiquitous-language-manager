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
 * 용어 변경 이력
 * @param changeHistoryId 
 * @param changeType 
 * @param reason 
 * @param createdAt 
 * @param termId 
 * @param previousStatus 
 * @param newStatus 
 * @param requestedBy 
 * @param approvedBy 
 * @param impactAnalysisId 
 */
data class TermChangeHistory(

    @get:JsonProperty("changeHistoryId", required = true) val changeHistoryId: kotlin.Long,

    @get:JsonProperty("changeType", required = true) val changeType: kotlin.String,

    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @get:JsonProperty("createdAt", required = true) val createdAt: java.time.OffsetDateTime,

    @get:JsonProperty("termId") val termId: kotlin.String? = null,

    @field:Valid
    @get:JsonProperty("previousStatus") val previousStatus: TermStatus? = null,

    @field:Valid
    @get:JsonProperty("newStatus") val newStatus: TermStatus? = null,

    @get:JsonProperty("requestedBy") val requestedBy: kotlin.String? = null,

    @get:JsonProperty("approvedBy") val approvedBy: kotlin.String? = null,

    @get:JsonProperty("impactAnalysisId") val impactAnalysisId: kotlin.String? = null
    ) {

}

