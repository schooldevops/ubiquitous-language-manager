package com.aulms.model

import java.util.Objects
import com.aulms.model.TermUploadStatus
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
 * 
 * @param lineNo 
 * @param status 
 * @param termId 
 * @param errorMessage 
 * @param registeredAt 
 * @param failedAt 
 */
data class TermUploadRow(

    @Schema(example = "1", required = true, description = "")
    @get:JsonProperty("lineNo", required = true) val lineNo: kotlin.Int,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("status", required = true) val status: TermUploadStatus,

    @Schema(example = "T-000001", description = "")
    @get:JsonProperty("termId") val termId: kotlin.String? = null,

    @Schema(example = "null", description = "")
    @get:JsonProperty("errorMessage") val errorMessage: kotlin.String? = null,

    @Schema(example = "null", description = "")
    @get:JsonProperty("registeredAt") val registeredAt: java.time.OffsetDateTime? = null,

    @Schema(example = "null", description = "")
    @get:JsonProperty("failedAt") val failedAt: java.time.OffsetDateTime? = null
    ) {

}

