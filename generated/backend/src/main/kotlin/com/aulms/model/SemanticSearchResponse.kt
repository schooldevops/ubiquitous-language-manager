package com.aulms.model

import java.util.Objects
import com.aulms.model.SemanticSearchResult
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
 * 자연어 의미 기반 용어 검색 응답
 * @param query 입력 질의문
 * @param items 의미 기반 표준 용어 후보
 */
data class SemanticSearchResponse(

    @Schema(example = "주문이 발생한 날짜", required = true, description = "입력 질의문")
    @get:JsonProperty("query", required = true) val query: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "의미 기반 표준 용어 후보")
    @get:JsonProperty("items", required = true) val items: kotlin.collections.List<SemanticSearchResult>
    ) {

}

