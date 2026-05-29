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
 * 검색어가 매칭된 용어 표현 정보
 * @param expressionType 
 * @param expressionValue 데이터 사전에 등록된 표현값
 * @param matchType 검색어와 표현값의 매칭 방식
 * @param inputExpression 사용자가 입력한 표현
 */
data class MatchedExpression(

    @field:Valid
    @get:JsonProperty("expressionType", required = true) val expressionType: ExpressionType,

    @get:JsonProperty("expressionValue", required = true) val expressionValue: kotlin.String,

    @get:JsonProperty("matchType", required = true) val matchType: MatchedExpression.MatchType,

    @get:JsonProperty("inputExpression") val inputExpression: kotlin.String? = null
    ) {

    /**
    * 검색어와 표현값의 매칭 방식
    * Values: Exact,Alias,Domain,Deprecated
    */
    enum class MatchType(@get:JsonValue val value: kotlin.String) {

        Exact("Exact"),
        Alias("Alias"),
        Domain("Domain"),
        Deprecated("Deprecated");

        companion object {
            @JvmStatic
            @JsonCreator
            fun forValue(value: kotlin.String): MatchType {
                return values().first{it -> it.value == value}
            }
        }
    }

}

