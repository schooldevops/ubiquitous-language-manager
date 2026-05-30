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
* 사용자 역할
* Values: ADMIN,DATA_STEWARD,DEVELOPMENT_LEAD,ARCHITECT,PLANNER,DEVELOPER,OPERATOR,VIEWER
*/
enum class UserRole(@get:JsonValue val value: kotlin.String) {

    ADMIN("ADMIN"),
    DATA_STEWARD("DATA_STEWARD"),
    DEVELOPMENT_LEAD("DEVELOPMENT_LEAD"),
    ARCHITECT("ARCHITECT"),
    PLANNER("PLANNER"),
    DEVELOPER("DEVELOPER"),
    OPERATOR("OPERATOR"),
    VIEWER("VIEWER");

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: kotlin.String): UserRole {
                return values().first{it -> it.value == value}
        }
    }
}

