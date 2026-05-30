package com.aulms.model

import java.util.Objects
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
 * 후보 검토 요청
 * @param reviewer 
 * @param decision 검토 결과
 * @param reason 
 */
data class TermCandidateReviewRequest(

    @get:Size(min=1)
    @Schema(example = "data.steward", required = true, description = "")
    @get:JsonProperty("reviewer", required = true) val reviewer: kotlin.String,

    @Schema(example = "Approve", required = true, description = "검토 결과")
    @get:JsonProperty("decision", required = true) val decision: TermCandidateReviewRequest.Decision,

    @get:Size(min=1)
    @Schema(example = "중복 없음, 약어와 타입 적합", required = true, description = "")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String
    ) {

    /**
    * 검토 결과
    * Values: Approve,Reject,RequestChanges
    */
    enum class Decision(@get:JsonValue val value: kotlin.String) {

        Approve("Approve"),
        Reject("Reject"),
        RequestChanges("RequestChanges");

        companion object {
            @JvmStatic
            @JsonCreator
            fun forValue(value: kotlin.String): Decision {
                return values().first{it -> it.value == value}
            }
        }
    }

}

