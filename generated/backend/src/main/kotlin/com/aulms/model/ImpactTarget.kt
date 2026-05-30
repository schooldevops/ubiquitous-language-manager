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
 * 영향 대상
 * @param targetType 
 * @param targetName 
 * @param systemCode 
 * @param location 
 * @param hop 
 * @param reason 
 */
data class ImpactTarget(

    @Schema(example = "API_FIELD", required = true, description = "")
    @get:JsonProperty("targetType", required = true) val targetType: ImpactTarget.TargetType,

    @Schema(example = "customerNumber", required = true, description = "")
    @get:JsonProperty("targetName", required = true) val targetName: kotlin.String,

    @Schema(example = "DICTIONARY", required = true, description = "")
    @get:JsonProperty("systemCode", required = true) val systemCode: kotlin.String,

    @Schema(example = "apiField:DICTIONARY.고객.customerNumber", required = true, description = "")
    @get:JsonProperty("location", required = true) val location: kotlin.String,

    @get:Min(1)
    @get:Max(2)
    @Schema(example = "1", required = true, description = "")
    @get:JsonProperty("hop", required = true) val hop: kotlin.Int,

    @Schema(example = "customerNumber API 필드가 고객번호를 표현함", required = true, description = "")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String
    ) {

    /**
    * 
    * Values: SYSTEM,DB_TABLE,DB_COLUMN,API,API_FIELD,DTO,DOCUMENT,TEST_CASE
    */
    enum class TargetType(@get:JsonValue val value: kotlin.String) {

        SYSTEM("SYSTEM"),
        DB_TABLE("DB_TABLE"),
        DB_COLUMN("DB_COLUMN"),
        API("API"),
        API_FIELD("API_FIELD"),
        DTO("DTO"),
        DOCUMENT("DOCUMENT"),
        TEST_CASE("TEST_CASE");

        companion object {
            @JvmStatic
            @JsonCreator
            fun forValue(value: kotlin.String): TargetType {
                return values().first{it -> it.value == value}
            }
        }
    }

}

