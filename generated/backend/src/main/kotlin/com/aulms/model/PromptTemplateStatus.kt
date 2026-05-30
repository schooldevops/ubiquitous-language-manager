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
import io.swagger.v3.oas.annotations.media.Schema

/**
* 프롬프트 템플릿 상태
* Values: Draft,Active,Deprecated
*/
enum class PromptTemplateStatus(@get:JsonValue val value: kotlin.String) {

    Draft("Draft"),
    Active("Active"),
    Deprecated("Deprecated");

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: kotlin.String): PromptTemplateStatus {
                return values().first{it -> it.value == value}
        }
    }
}

