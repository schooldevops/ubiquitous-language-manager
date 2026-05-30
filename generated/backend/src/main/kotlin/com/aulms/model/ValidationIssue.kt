package com.aulms.model

import java.util.Objects
import com.aulms.model.ValidationSeverity
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
 * 
 * @param severity 
 * @param source 문서, DDL, OpenAPI, 코드, PR 등 검증 출처
 * @param inputExpression 입력 표현
 * @param reason 위반 또는 권고 사유
 * @param location 파일명, 라인, 필드명 등 위치 정보
 * @param standardTerm 표준 용어
 * @param recommendedExpression 권장 표현
 */
data class ValidationIssue(

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("severity", required = true) val severity: ValidationSeverity,

    @Schema(example = "CODE", required = true, description = "문서, DDL, OpenAPI, 코드, PR 등 검증 출처")
    @get:JsonProperty("source", required = true) val source: kotlin.String,

    @Schema(example = "customerId", required = true, description = "입력 표현")
    @get:JsonProperty("inputExpression", required = true) val inputExpression: kotlin.String,

    @Schema(example = "고객번호의 표준 API/코드 표현은 customerNumber입니다.", required = true, description = "위반 또는 권고 사유")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @Schema(example = "CustomerOrderResponse.kt:12", description = "파일명, 라인, 필드명 등 위치 정보")
    @get:JsonProperty("location") val location: kotlin.String? = null,

    @Schema(example = "고객번호", description = "표준 용어")
    @get:JsonProperty("standardTerm") val standardTerm: kotlin.String? = null,

    @Schema(example = "customerNumber", description = "권장 표현")
    @get:JsonProperty("recommendedExpression") val recommendedExpression: kotlin.String? = null
    ) {

}

