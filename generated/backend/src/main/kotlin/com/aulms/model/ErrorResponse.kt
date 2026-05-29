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
 * 
 * @param code 시스템에서 정의한 에러 코드
 * @param message 사용자 또는 개발자가 이해할 수 있는 에러 메시지
 * @param traceId 요청 추적 ID
 * @param detail 상세 에러 설명
 */
data class ErrorResponse(

    @get:JsonProperty("code", required = true) val code: kotlin.String,

    @get:JsonProperty("message", required = true) val message: kotlin.String,

    @get:JsonProperty("traceId", required = true) val traceId: kotlin.String,

    @get:JsonProperty("detail") val detail: kotlin.String? = null
    ) {

}

