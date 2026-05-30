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
 * 
 * @param code 시스템에서 정의한 에러 코드
 * @param message 사용자 또는 개발자가 이해할 수 있는 에러 메시지
 * @param traceId 요청 추적 ID
 * @param detail 상세 에러 설명
 */
data class ErrorResponse(

    @Schema(example = "TERM_NOT_FOUND", required = true, description = "시스템에서 정의한 에러 코드")
    @get:JsonProperty("code", required = true) val code: kotlin.String,

    @Schema(example = "표준 용어를 찾을 수 없습니다.", required = true, description = "사용자 또는 개발자가 이해할 수 있는 에러 메시지")
    @get:JsonProperty("message", required = true) val message: kotlin.String,

    @Schema(example = "01HX8R7P9Q6RZC6Q9VQ0X7Z3WB", required = true, description = "요청 추적 ID")
    @get:JsonProperty("traceId", required = true) val traceId: kotlin.String,

    @Schema(example = "termId=T-000123", description = "상세 에러 설명")
    @get:JsonProperty("detail") val detail: kotlin.String? = null
    ) {

}

