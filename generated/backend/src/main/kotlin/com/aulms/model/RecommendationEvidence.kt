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
 * RAG 검색으로 수집한 근거 용어
 * @param termId 
 * @param standardTerm 
 * @param englishName 
 * @param dbColumn 
 * @param apiField 
 * @param domainName 
 * @param source 
 * @param score 
 * @param reason 
 */
data class RecommendationEvidence(

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

    @Schema(example = "고객", required = true, description = "")
    @get:JsonProperty("domainName", required = true) val domainName: kotlin.String,

    @Schema(example = "Semantic", required = true, description = "")
    @get:JsonProperty("source", required = true) val source: RecommendationEvidence.Source,

    @Schema(example = "0.92", required = true, description = "")
    @get:JsonProperty("score", required = true) val score: kotlin.Double,

    @Schema(example = "고객 도메인, 배송 시간, 선호 의미가 기존 고객 표준 용어와 가장 가깝고 배송 맥락이 추가됨", required = true, description = "")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String
    ) {

    /**
    * 
    * Values: Exact,Alias,Semantic
    */
    enum class Source(@get:JsonValue val value: kotlin.String) {

        Exact("Exact"),
        Alias("Alias"),
        Semantic("Semantic");

        companion object {
            @JvmStatic
            @JsonCreator
            fun forValue(value: kotlin.String): Source {
                return values().first{it -> it.value == value}
            }
        }
    }

}

