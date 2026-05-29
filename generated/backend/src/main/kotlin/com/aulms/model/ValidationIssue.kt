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
    @get:JsonProperty("severity", required = true) val severity: ValidationSeverity,

    @get:JsonProperty("source", required = true) val source: kotlin.String,

    @get:JsonProperty("inputExpression", required = true) val inputExpression: kotlin.String,

    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @get:JsonProperty("location") val location: kotlin.String? = null,

    @get:JsonProperty("standardTerm") val standardTerm: kotlin.String? = null,

    @get:JsonProperty("recommendedExpression") val recommendedExpression: kotlin.String? = null
    ) {

}

