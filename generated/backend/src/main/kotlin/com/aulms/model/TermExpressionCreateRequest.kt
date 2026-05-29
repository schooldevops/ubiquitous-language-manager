package com.aulms.model

import java.util.Objects
import com.aulms.model.ExpressionType
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
 * 용어 표현 매핑 등록 요청
 * @param expressionType 
 * @param expressionValue 
 * @param isStandard 
 * @param language 
 * @param style 
 */
data class TermExpressionCreateRequest(

    @field:Valid
    @get:JsonProperty("expressionType", required = true) val expressionType: ExpressionType,

    @get:Size(min=1,max=500)
    @get:JsonProperty("expressionValue", required = true) val expressionValue: kotlin.String,

    @get:JsonProperty("isStandard", required = true) val isStandard: kotlin.Boolean,

    @get:Size(max=20)
    @get:JsonProperty("language") val language: kotlin.String? = null,

    @get:Size(max=50)
    @get:JsonProperty("style") val style: kotlin.String? = null
    ) {

}

