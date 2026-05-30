package com.aulms.model

import java.util.Objects
import com.aulms.model.ColumnSystemUsage
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
 * 컬럼 사용 시스템 조회 응답
 * @param columnName 
 * @param items 
 */
data class ColumnSystemUsageResponse(

    @Schema(example = "CUST_NO", required = true, description = "")
    @get:JsonProperty("columnName", required = true) val columnName: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("items", required = true) val items: kotlin.collections.List<ColumnSystemUsage>
    ) {

}

