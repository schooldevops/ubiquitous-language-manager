package com.aulms.model

import java.util.Objects
import com.aulms.model.TermStatus
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
 * 자연어 의미 기반 용어 검색 요청
 * @param query 자연어 질의문
 * @param domainNames 검색 대상 도메인 필터
 * @param statuses 검색 대상 용어 상태 필터
 * @param limit 반환할 후보 개수
 */
data class SemanticSearchRequest(

    @get:Size(min=1)
    @Schema(example = "주문이 발생한 날짜", required = true, description = "자연어 질의문")
    @get:JsonProperty("query", required = true) val query: kotlin.String,

    @Schema(example = "[\"주문\"]", description = "검색 대상 도메인 필터")
    @get:JsonProperty("domainNames") val domainNames: kotlin.collections.List<kotlin.String>? = null,

    @field:Valid
    @Schema(example = "[\"Approved\"]", description = "검색 대상 용어 상태 필터")
    @get:JsonProperty("statuses") val statuses: kotlin.collections.List<TermStatus>? = null,

    @get:Min(1)
    @get:Max(50)
    @Schema(example = "5", description = "반환할 후보 개수")
    @get:JsonProperty("limit") val limit: kotlin.Int? = 5
    ) {

}

