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
 * 프롬프트 템플릿 변수
 * @param name 
 * @param description 
 * @param required 
 * @param source 변수 값 출처
 */
data class PromptTemplateVariable(

    @get:JsonProperty("name", required = true) val name: kotlin.String,

    @get:JsonProperty("description", required = true) val description: kotlin.String,

    @get:JsonProperty("required", required = true) val required: kotlin.Boolean,

    @get:JsonProperty("source", required = true) val source: PromptTemplateVariable.Source
    ) {

    /**
    * 변수 값 출처
    * Values: UserInput,DictionarySearch,CandidateWorkflow,System
    */
    enum class Source(@get:JsonValue val value: kotlin.String) {

        UserInput("UserInput"),
        DictionarySearch("DictionarySearch"),
        CandidateWorkflow("CandidateWorkflow"),
        System("System");

        companion object {
            @JvmStatic
            @JsonCreator
            fun forValue(value: kotlin.String): Source {
                return values().first{it -> it.value == value}
            }
        }
    }

}

