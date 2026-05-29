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

    @get:JsonProperty("concept", required = true) val concept: kotlin.String,

    @get:JsonProperty("standardTerm", required = true) val standardTerm: kotlin.String,

    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @get:JsonProperty("dbColumn", required = true) val dbColumn: kotlin.String,

    @get:JsonProperty("apiField", required = true) val apiField: kotlin.String,

    @get:JsonProperty("codeVariable", required = true) val codeVariable: kotlin.String,

    @field:Valid
    @get:JsonProperty("status", required = true) val status: TermStatus
    ) {

}

