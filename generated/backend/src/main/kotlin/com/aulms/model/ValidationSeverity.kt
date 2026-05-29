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
* 검증 결과 심각도
* Values: ERROR,WARNING,INFO
*/
enum class ValidationSeverity(@get:JsonValue val value: kotlin.String) {

    ERROR("ERROR"),
    WARNING("WARNING"),
    INFO("INFO");

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: kotlin.String): ValidationSeverity {
                return values().first{it -> it.value == value}
        }
    }
}

