package com.aulms.model

import java.util.Objects
import com.aulms.model.Term
import com.aulms.model.TermCandidate
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
 * 후보 승격 결과
 * @param candidate 
 * @param term 
 */
data class CandidatePromotionResult(

    @field:Valid
    @get:JsonProperty("candidate", required = true) val candidate: TermCandidate,

    @field:Valid
    @get:JsonProperty("term", required = true) val term: Term
    ) {

}

