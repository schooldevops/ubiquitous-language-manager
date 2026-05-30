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
 * 용어 승인 요청
 * @param approver 
 * @param reason 
 */
data class TermApprovalRequest(

    @get:Size(min=1)
    @Schema(example = "data.steward", required = true, description = "")
    @get:JsonProperty("approver", required = true) val approver: kotlin.String,

    @get:Size(min=1)
    @Schema(example = "업무 정의와 산출물 표현 검토 완료", required = true, description = "")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String
    ) {

}

