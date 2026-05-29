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

/**
 * 기획서 본문에서 검출된 용어 표현
 * @param expression 본문에서 검출된 표현
 * @param sentenceIndex 문장 번호
 * @param startOffset 본문 내 시작 위치
 * @param endOffset 본문 내 종료 위치
 * @param matchType 검출 방식
 * @param mappedTermId 매핑된 표준 용어 ID
 * @param standardTerm 매핑된 표준 용어명
 */
data class DetectedTerm(

    @get:JsonProperty("expression", required = true) val expression: kotlin.String,

    @get:Min(0)
    @get:JsonProperty("sentenceIndex", required = true) val sentenceIndex: kotlin.Int,

    @get:Min(0)
    @get:JsonProperty("startOffset", required = true) val startOffset: kotlin.Int,

    @get:Min(0)
    @get:JsonProperty("endOffset", required = true) val endOffset: kotlin.Int,

    @get:JsonProperty("matchType", required = true) val matchType: DetectedTerm.MatchType,

    @get:JsonProperty("mappedTermId") val mappedTermId: kotlin.String? = null,

    @get:JsonProperty("standardTerm") val standardTerm: kotlin.String? = null
    ) {

    /**
    * 검출 방식
    * Values: Exact,Alias,Candidate
    */
    enum class MatchType(@get:JsonValue val value: kotlin.String) {

        Exact("Exact"),
        Alias("Alias"),
        Candidate("Candidate");

        companion object {
            @JvmStatic
            @JsonCreator
            fun forValue(value: kotlin.String): MatchType {
                return values().first{it -> it.value == value}
            }
        }
    }

}

