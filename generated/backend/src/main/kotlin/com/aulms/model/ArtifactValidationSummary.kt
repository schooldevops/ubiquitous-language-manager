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
 * 검증 결과 집계
 * @param errorCount 
 * @param warningCount 
 * @param infoCount 
 */
data class ArtifactValidationSummary(

    @get:Min(0)
    @Schema(example = "1", required = true, description = "")
    @get:JsonProperty("errorCount", required = true) val errorCount: kotlin.Int,

    @get:Min(0)
    @Schema(example = "1", required = true, description = "")
    @get:JsonProperty("warningCount", required = true) val warningCount: kotlin.Int,

    @get:Min(0)
    @Schema(example = "0", required = true, description = "")
    @get:JsonProperty("infoCount", required = true) val infoCount: kotlin.Int
    ) {

}

