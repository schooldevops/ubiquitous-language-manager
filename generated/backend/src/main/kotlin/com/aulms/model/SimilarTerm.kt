package com.aulms.model

import java.util.Objects
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
 * 신규 후보와 유사한 기존 용어
 * @param termId 
 * @param koreanName 
 * @param englishName 
 * @param dbColumn 
 * @param apiField 
 * @param reason 
 */
data class SimilarTerm(

    @Schema(example = "T-000001", required = true, description = "")
    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @Schema(example = "고객번호", required = true, description = "")
    @get:JsonProperty("koreanName", required = true) val koreanName: kotlin.String,

    @Schema(example = "Customer Number", required = true, description = "")
    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @Schema(example = "CUST_NO", required = true, description = "")
    @get:JsonProperty("dbColumn", required = true) val dbColumn: kotlin.String,

    @Schema(example = "customerNumber", required = true, description = "")
    @get:JsonProperty("apiField", required = true) val apiField: kotlin.String,

    @Schema(example = "같은 고객 도메인의 기존 표준 용어", required = true, description = "")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String
    ) {

}

