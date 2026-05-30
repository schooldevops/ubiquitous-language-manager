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
 * 용어 관계 검색 결과
 * @param termId 
 * @param standardTerm 
 * @param englishName 
 * @param relationshipType 
 * @param direction 
 * @param reason 
 */
data class RelationshipSearchResult(

    @Schema(example = "T-000004", required = true, description = "")
    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @Schema(example = "주문번호", required = true, description = "")
    @get:JsonProperty("standardTerm", required = true) val standardTerm: kotlin.String,

    @Schema(example = "Order Number", required = true, description = "")
    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @Schema(example = "usedWith", required = true, description = "")
    @get:JsonProperty("relationshipType", required = true) val relationshipType: kotlin.String,

    @Schema(example = "OUTGOING", required = true, description = "")
    @get:JsonProperty("direction", required = true) val direction: RelationshipSearchResult.Direction,

    @Schema(example = "고객번호는 주문번호와 함께 주문 조회 조건과 응답에서 사용", required = true, description = "")
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

