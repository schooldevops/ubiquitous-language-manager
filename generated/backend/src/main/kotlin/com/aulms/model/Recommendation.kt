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
 * 표준 용어 사용 권고
 * @param action 권고 조치
 * @param recommendedExpression 권고 표준 표현
 * @param reason 권고 사유
 * @param recommendedTermId 권고 표준 용어 식별자
 * @param recommendedTerm 권고 표준 용어명
 * @param severity 권고 중요도
 */
data class Recommendation(

    @get:JsonProperty("action", required = true) val action: Recommendation.Action,

    @get:JsonProperty("recommendedExpression", required = true) val recommendedExpression: kotlin.String,

    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @get:JsonProperty("recommendedTermId") val recommendedTermId: kotlin.String? = null,

    @get:JsonProperty("recommendedTerm") val recommendedTerm: kotlin.String? = null,

    @get:JsonProperty("severity") val severity: Recommendation.Severity? = null
    ) {

    /**
    * 권고 조치
    * Values: UseStandardTerm,ReplaceDeprecatedTerm,ConfirmContext,RegisterCandidate
    */
    enum class Action(@get:JsonValue val value: kotlin.String) {

        UseStandardTerm("UseStandardTerm"),
        ReplaceDeprecatedTerm("ReplaceDeprecatedTerm"),
        ConfirmContext("ConfirmContext"),
        RegisterCandidate("RegisterCandidate");

        companion object {
            @JvmStatic
            @JsonCreator
            fun forValue(value: kotlin.String): Action {
                return values().first{it -> it.value == value}
            }
        }
    }

    /**
    * 권고 중요도
    * Values: Info,Warning,Error
    */
    enum class Severity(@get:JsonValue val value: kotlin.String) {

        Info("Info"),
        Warning("Warning"),
        Error("Error");

        companion object {
            @JvmStatic
            @JsonCreator
            fun forValue(value: kotlin.String): Severity {
                return values().first{it -> it.value == value}
            }
        }
    }

}

