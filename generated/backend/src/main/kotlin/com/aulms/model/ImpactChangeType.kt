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
* 영향도 분석 대상 변경 유형
* Values: DESCRIPTION_UPDATE,ALIAS_ADD,API_FIELD_RENAME,DB_COLUMN_RENAME,PHYSICAL_TYPE_CHANGE,DIGITS_CHANGE,DEPRECATE_TERM
*/
enum class ImpactChangeType(@get:JsonValue val value: kotlin.String) {

    DESCRIPTION_UPDATE("DESCRIPTION_UPDATE"),
    ALIAS_ADD("ALIAS_ADD"),
    API_FIELD_RENAME("API_FIELD_RENAME"),
    DB_COLUMN_RENAME("DB_COLUMN_RENAME"),
    PHYSICAL_TYPE_CHANGE("PHYSICAL_TYPE_CHANGE"),
    DIGITS_CHANGE("DIGITS_CHANGE"),
    DEPRECATE_TERM("DEPRECATE_TERM");

    companion object {
        @JvmStatic
        @JsonCreator
        fun forValue(value: kotlin.String): ImpactChangeType {
                return values().first{it -> it.value == value}
        }
    }
}

