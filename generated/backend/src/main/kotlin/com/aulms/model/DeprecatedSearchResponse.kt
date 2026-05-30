package com.aulms.model

import java.util.Objects
import com.aulms.model.DeprecatedSearchResult
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
 * 폐기어 검색 결과 목록 응답
 * @param query 사용자가 입력한 폐기어 또는 비표준 표현
 * @param items 폐기어와 대체 표준 용어 목록
 */
data class DeprecatedSearchResponse(

    @Schema(example = "CUST_ID", required = true, description = "사용자가 입력한 폐기어 또는 비표준 표현")
    @get:JsonProperty("query", required = true) val query: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "폐기어와 대체 표준 용어 목록")
    @get:JsonProperty("items", required = true) val items: kotlin.collections.List<DeprecatedSearchResult>
    ) {

}

