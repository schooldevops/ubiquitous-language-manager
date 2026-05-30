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
 * 표준 용어 사용 권고
 * @param action 권고 조치
 * @param recommendedExpression 권고 표준 표현
 * @param reason 권고 사유
 * @param recommendedTermId 권고 표준 용어 식별자
 * @param recommendedTerm 권고 표준 용어명
 * @param severity 권고 중요도
 */
data class Recommendation(

    @Schema(example = "UseStandardTerm", required = true, description = "권고 조치")
    @get:JsonProperty("action", required = true) val action: Recommendation.Action,

    @Schema(example = "customerNumber", required = true, description = "권고 표준 표현")
    @get:JsonProperty("recommendedExpression", required = true) val recommendedExpression: kotlin.String,

    @Schema(example = "고객ID는 고객번호의 유사어이며 표준 API 필드명은 customerNumber임", required = true, description = "권고 사유")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @Schema(example = "T-000001", description = "권고 표준 용어 식별자")
    @get:JsonProperty("recommendedTermId") val recommendedTermId: kotlin.String? = null,

    @Schema(example = "고객번호", description = "권고 표준 용어명")
    @get:JsonProperty("recommendedTerm") val recommendedTerm: kotlin.String? = null,

    @Schema(example = "Warning", description = "권고 중요도")
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

