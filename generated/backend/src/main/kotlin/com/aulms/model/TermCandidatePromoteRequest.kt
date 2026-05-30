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
 * 후보 표준 용어 승격 요청
 * @param approver 
 * @param owner 
 * @param reason 
 */
data class TermCandidatePromoteRequest(

    @get:Size(min=1)
    @Schema(example = "data.steward", required = true, description = "")
    @get:JsonProperty("approver", required = true) val approver: kotlin.String,

    @get:Size(min=1)
    @Schema(example = "고객도메인 데이터스튜어드", required = true, description = "")
    @get:JsonProperty("owner", required = true) val owner: kotlin.String,

    @get:Size(min=1)
    @Schema(example = "표준 용어로 승인 및 개발 반영 가능", required = true, description = "")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String
    ) {

}

