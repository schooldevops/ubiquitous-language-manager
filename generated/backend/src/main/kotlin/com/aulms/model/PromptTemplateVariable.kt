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
 * 프롬프트 템플릿 변수
 * @param name 
 * @param description 
 * @param required 
 * @param source 변수 값 출처
 */
data class PromptTemplateVariable(

    @Schema(example = "termMappings", required = true, description = "")
    @get:JsonProperty("name", required = true) val name: kotlin.String,

    @Schema(example = "데이터 사전 검색 결과로 확정된 표준 용어 매핑 목록", required = true, description = "")
    @get:JsonProperty("description", required = true) val description: kotlin.String,

    @Schema(example = "true", required = true, description = "")
    @get:JsonProperty("required", required = true) val required: kotlin.Boolean,

    @Schema(example = "DictionarySearch", required = true, description = "변수 값 출처")
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

