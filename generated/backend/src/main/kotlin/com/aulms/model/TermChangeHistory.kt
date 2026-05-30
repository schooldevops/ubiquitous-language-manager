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
import io.swagger.v3.oas.annotations.media.Schema

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

    @Schema(example = "1", required = true, description = "")
    @get:JsonProperty("changeHistoryId", required = true) val changeHistoryId: kotlin.Long,

    @Schema(example = "APPROVE", required = true, description = "")
    @get:JsonProperty("changeType", required = true) val changeType: kotlin.String,

    @Schema(example = "업무 정의와 산출물 표현 검토 완료", required = true, description = "")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("createdAt", required = true) val createdAt: java.time.OffsetDateTime,

    @Schema(example = "T-000001", description = "")
    @get:JsonProperty("termId") val termId: kotlin.String? = null,

    @field:Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("previousStatus") val previousStatus: TermStatus? = null,

    @field:Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("newStatus") val newStatus: TermStatus? = null,

    @Schema(example = "planner", description = "")
    @get:JsonProperty("requestedBy") val requestedBy: kotlin.String? = null,

    @Schema(example = "data.steward", description = "")
    @get:JsonProperty("approvedBy") val approvedBy: kotlin.String? = null,

    @Schema(example = "IA-000001", description = "")
    @get:JsonProperty("impactAnalysisId") val impactAnalysisId: kotlin.String? = null
    ) {

}

