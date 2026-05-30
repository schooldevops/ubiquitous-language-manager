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
* 프롬프트 템플릿 유형
* Values: VibeCoding,Planning
*/
enum class PromptTemplateType(@get:JsonValue val value: kotlin.String) {

    VibeCoding("VibeCoding"),
    Planning("Planning");

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: kotlin.String): PromptTemplateType {
                return values().first{it -> it.value == value}
        }
    }
}

