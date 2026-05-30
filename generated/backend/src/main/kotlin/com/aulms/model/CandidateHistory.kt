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
 * 후보 상태 변경 이력
 * @param historyId 
 * @param candidateId 
 * @param status 
 * @param reason 
 * @param actor 
 * @param createdAt 
 */
data class CandidateHistory(

    @Schema(example = "CAND-HIST-000001", required = true, description = "")
    @get:JsonProperty("historyId", required = true) val historyId: kotlin.String,

    @Schema(example = "CAND-000001", required = true, description = "")
    @get:JsonProperty("candidateId", required = true) val candidateId: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("status", required = true) val status: CandidateStatus,

    @Schema(example = "신규 후보 등록", required = true, description = "")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @Schema(example = "planner01", required = true, description = "")
    @get:JsonProperty("actor", required = true) val actor: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("createdAt", required = true) val createdAt: java.time.OffsetDateTime
    ) {

}

