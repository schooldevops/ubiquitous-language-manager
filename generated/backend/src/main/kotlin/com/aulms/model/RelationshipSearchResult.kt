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

/**
 * 용어 관계 검색 결과
 * @param termId 
 * @param standardTerm 
 * @param englishName 
 * @param relationshipType 
 * @param direction 
 * @param reason 
 */
data class RelationshipSearchResult(

    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @get:JsonProperty("standardTerm", required = true) val standardTerm: kotlin.String,

    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @get:JsonProperty("relationshipType", required = true) val relationshipType: kotlin.String,

    @get:JsonProperty("direction", required = true) val direction: RelationshipSearchResult.Direction,

    @get:JsonProperty("reason", required = true) val reason: kotlin.String
    ) {

    /**
    * 
    * Values: OUTGOING,INCOMING
    */
    enum class Direction(@get:JsonValue val value: kotlin.String) {

        OUTGOING("OUTGOING"),
        INCOMING("INCOMING");

        companion object {
            @JvmStatic
            @JsonCreator
            fun forValue(value: kotlin.String): Direction {
                return values().first{it -> it.value == value}
            }
        }
    }

}

