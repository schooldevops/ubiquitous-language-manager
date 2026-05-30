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
* 별칭 유형
* Values: Synonym,Forbidden,Deprecated,NeedsContext
*/
enum class AliasType(@get:JsonValue val value: kotlin.String) {

    Synonym("Synonym"),
    Forbidden("Forbidden"),
    Deprecated("Deprecated"),
    NeedsContext("NeedsContext");

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: kotlin.String): AliasType {
                return values().first{it -> it.value == value}
        }
    }
}

