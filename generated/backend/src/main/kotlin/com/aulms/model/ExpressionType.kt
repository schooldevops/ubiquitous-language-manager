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
* 용어 표현 유형
* Values: Korean,English,DB_COLUMN,API_FIELD,CODE_VARIABLE,UI_LABEL,TEST_WORD
*/
enum class ExpressionType(@get:JsonValue val value: kotlin.String) {

    Korean("Korean"),
    English("English"),
    DB_COLUMN("DB_COLUMN"),
    API_FIELD("API_FIELD"),
    CODE_VARIABLE("CODE_VARIABLE"),
    UI_LABEL("UI_LABEL"),
    TEST_WORD("TEST_WORD");

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: kotlin.String): ExpressionType {
                return values().first{it -> it.value == value}
        }
    }
}

