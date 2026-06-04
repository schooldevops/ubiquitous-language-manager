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
 * 도메인별 용어 수와 상태별 분포
 * @param domainName 
 * @param totalCount 도메인 전체 용어 수
 * @param approvedCount 확정(Approved) 용어 수
 * @param draftCount 
 * @param reviewingCount 
 * @param deprecatedCount 
 * @param rejectedCount 
 */
data class DomainTermStat(

    @Schema(example = "고객", required = true, description = "")
    @get:JsonProperty("domainName", required = true) val domainName: kotlin.String,

    @Schema(example = "3", required = true, description = "도메인 전체 용어 수")
    @get:JsonProperty("totalCount", required = true) val totalCount: kotlin.Int,

    @Schema(example = "3", required = true, description = "확정(Approved) 용어 수")
    @get:JsonProperty("approvedCount", required = true) val approvedCount: kotlin.Int,

    @Schema(example = "0", required = true, description = "")
    @get:JsonProperty("draftCount", required = true) val draftCount: kotlin.Int,

    @Schema(example = "0", required = true, description = "")
    @get:JsonProperty("reviewingCount", required = true) val reviewingCount: kotlin.Int,

    @Schema(example = "0", required = true, description = "")
    @get:JsonProperty("deprecatedCount", required = true) val deprecatedCount: kotlin.Int,

    @Schema(example = "0", required = true, description = "")
    @get:JsonProperty("rejectedCount", required = true) val rejectedCount: kotlin.Int
    ) {

}

