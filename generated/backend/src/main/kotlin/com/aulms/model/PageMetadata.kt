package com.aulms.model

import java.util.Objects
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
 * 
 * @param page 현재 페이지 번호
 * @param propertySize 페이지 크기
 * @param totalElements 전체 항목 수
 * @param totalPages 전체 페이지 수
 */
data class PageMetadata(

    @get:Min(0)
    @Schema(example = "0", required = true, description = "현재 페이지 번호")
    @get:JsonProperty("page", required = true) val page: kotlin.Int,

    @get:Min(1)
    @Schema(example = "20", required = true, description = "페이지 크기")
    @get:JsonProperty("size", required = true) val propertySize: kotlin.Int,

    @get:Min(0L)
    @Schema(example = "120", required = true, description = "전체 항목 수")
    @get:JsonProperty("totalElements", required = true) val totalElements: kotlin.Long,

    @get:Min(0)
    @Schema(example = "6", required = true, description = "전체 페이지 수")
    @get:JsonProperty("totalPages", required = true) val totalPages: kotlin.Int
    ) {

}

