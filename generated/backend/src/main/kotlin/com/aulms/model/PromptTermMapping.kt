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
 * 프롬프트에 주입할 표준 용어 매핑
 * @param concept 
 * @param standardTerm 
 * @param englishName 
 * @param dbColumn 
 * @param apiField 
 * @param codeVariable 
 * @param status 
 */
data class PromptTermMapping(

    @Schema(example = "고객번호", required = true, description = "")
    @get:JsonProperty("concept", required = true) val concept: kotlin.String,

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
    @get:JsonProperty("status", required = true) val status: TermStatus
    ) {

}

