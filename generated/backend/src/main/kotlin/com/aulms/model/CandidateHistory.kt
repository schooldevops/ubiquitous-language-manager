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
 * 후보 상태 변경 이력
 * @param historyId 
 * @param candidateId 
 * @param status 
 * @param reason 
 * @param actor 
 * @param createdAt 
 */
data class CandidateHistory(

    @get:JsonProperty("historyId", required = true) val historyId: kotlin.String,

    @get:JsonProperty("candidateId", required = true) val candidateId: kotlin.String,

    @field:Valid
    @get:JsonProperty("status", required = true) val status: CandidateStatus,

    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @get:JsonProperty("actor", required = true) val actor: kotlin.String,

    @get:JsonProperty("createdAt", required = true) val createdAt: java.time.OffsetDateTime
    ) {

}

