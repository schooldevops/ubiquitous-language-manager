package com.aulms.model

import java.util.Objects
import com.aulms.model.TermStatus
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
 * 표준 용어 매핑 결과
 * @param concept 
 * @param termId 
 * @param standardTerm 
 * @param englishName 
 * @param dbColumn 
 * @param apiField 
 * @param codeVariable 
 * @param status 
 * @param mappingSource 매핑 출처
 * @param recommendationReason 
 */
data class AssistTermMapping(

    @get:JsonProperty("concept", required = true) val concept: kotlin.String,

    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @get:JsonProperty("standardTerm", required = true) val standardTerm: kotlin.String,

    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @get:JsonProperty("dbColumn", required = true) val dbColumn: kotlin.String,

    @get:JsonProperty("apiField", required = true) val apiField: kotlin.String,

    @get:JsonProperty("codeVariable", required = true) val codeVariable: kotlin.String,

    @field:Valid
    @get:JsonProperty("status", required = true) val status: TermStatus,

    @get:JsonProperty("mappingSource", required = true) val mappingSource: AssistTermMapping.MappingSource,

    @get:JsonProperty("recommendationReason", required = true) val recommendationReason: kotlin.String
    ) {

    /**
    * 매핑 출처
    * Values: Exact,Alias,Semantic
    */
    enum class MappingSource(@get:JsonValue val value: kotlin.String) {

        Exact("Exact"),
        Alias("Alias"),
        Semantic("Semantic");

        companion object {
            @JvmStatic
            @JsonCreator
            fun forValue(value: kotlin.String): MappingSource {
                return values().first{it -> it.value == value}
            }
        }
    }

}

