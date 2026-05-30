package com.aulms.model

import java.util.Objects
import com.aulms.model.PageMetadata
import com.aulms.model.SearchResult
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
 * 검색 결과 목록 응답
 * @param query 사용자가 입력한 검색어
 * @param items 검색 결과 목록
 * @param page 
 */
data class SearchResponse(

    @Schema(example = "고객ID", required = true, description = "사용자가 입력한 검색어")
    @get:JsonProperty("query", required = true) val query: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "검색 결과 목록")
    @get:JsonProperty("items", required = true) val items: kotlin.collections.List<SearchResult>,

    @field:Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("page") val page: PageMetadata? = null
    ) {

}

