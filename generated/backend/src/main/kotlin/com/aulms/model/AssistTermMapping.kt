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
import io.swagger.v3.oas.annotations.media.Schema

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

    @Schema(example = "고객번호", required = true, description = "")
    @get:JsonProperty("concept", required = true) val concept: kotlin.String,

    @Schema(example = "T-000001", required = true, description = "")
    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @Schema(example = "고객번호", required = true, description = "")
    @get:JsonProperty("standardTerm", required = true) val standardTerm: kotlin.String,

    @Schema(example = "Customer Number", required = true, description = "")
    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @Schema(example = "CUST_NO", required = true, description = "")
    @get:JsonProperty("dbColumn", required = true) val dbColumn: kotlin.String,

    @Schema(example = "customerNumber", required = true, description = "")
    @get:JsonProperty("apiField", required = true) val apiField: kotlin.String,

    @Schema(example = "customerNumber", required = true, description = "")
    @get:JsonProperty("codeVariable", required = true) val codeVariable: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("status", required = true) val status: TermStatus,

    @Schema(example = "Exact", required = true, description = "매핑 출처")
    @get:JsonProperty("mappingSource", required = true) val mappingSource: AssistTermMapping.MappingSource,

    @Schema(example = "승인된 표준 용어 매핑", required = true, description = "")
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

