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
 * 후보 표준 용어 승격 요청
 * @param approver 
 * @param owner 
 * @param reason 
 */
data class TermCandidatePromoteRequest(

    @get:Size(min=1)
    @get:JsonProperty("approver", required = true) val approver: kotlin.String,

    @get:Size(min=1)
    @get:JsonProperty("owner", required = true) val owner: kotlin.String,

    @get:Size(min=1)
    @get:JsonProperty("reason", required = true) val reason: kotlin.String
    ) {

}

