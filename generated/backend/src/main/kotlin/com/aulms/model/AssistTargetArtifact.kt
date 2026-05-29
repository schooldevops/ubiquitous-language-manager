package com.aulms.model

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.annotation.JsonCreator
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
* AI 지원 API가 생성할 산출물 유형
* Values: DTO,OPENAPI_SCHEMA,SQL_EXAMPLE
*/
enum class AssistTargetArtifact(@get:JsonValue val value: kotlin.String) {

    DTO("DTO"),
    OPENAPI_SCHEMA("OPENAPI_SCHEMA"),
    SQL_EXAMPLE("SQL_EXAMPLE");

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: kotlin.String): AssistTargetArtifact {
                return values().first{it -> it.value == value}
        }
    }
}

