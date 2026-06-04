package com.aulms.model

import java.util.Objects
import com.aulms.model.TermUploadRow
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
 * @param uploadBatchId 
 * @param uploadedAt 
 * @param totalRows 
 * @param inserted 
 * @param updated 
 * @param failed 
 * @param rows 
 */
data class TermUploadResult(

    @Schema(example = "UPL-20260604-0001", required = true, description = "")
    @get:JsonProperty("uploadBatchId", required = true) val uploadBatchId: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("uploadedAt", required = true) val uploadedAt: java.time.OffsetDateTime,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("totalRows", required = true) val totalRows: kotlin.Int,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("inserted", required = true) val inserted: kotlin.Int,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("updated", required = true) val updated: kotlin.Int,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("failed", required = true) val failed: kotlin.Int,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("rows", required = true) val rows: kotlin.collections.List<TermUploadRow>
    ) {

}

