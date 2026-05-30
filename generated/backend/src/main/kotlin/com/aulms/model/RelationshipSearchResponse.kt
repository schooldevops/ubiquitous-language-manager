package com.aulms.model

import java.util.Objects
import com.aulms.model.RelationshipPath
import com.aulms.model.RelationshipSearchResult
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
 * Graphify 관계 검색 응답
 * @param query 검색 기준
 * @param items 관계 검색 결과
 * @param paths Graphify 그래프 경로
 */
data class RelationshipSearchResponse(

    @Schema(example = "T-000001", required = true, description = "검색 기준")
    @get:JsonProperty("query", required = true) val query: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "관계 검색 결과")
    @get:JsonProperty("items", required = true) val items: kotlin.collections.List<RelationshipSearchResult>,

    @field:Valid
    @Schema(example = "null", required = true, description = "Graphify 그래프 경로")
    @get:JsonProperty("paths", required = true) val paths: kotlin.collections.List<RelationshipPath>
    ) {

}

