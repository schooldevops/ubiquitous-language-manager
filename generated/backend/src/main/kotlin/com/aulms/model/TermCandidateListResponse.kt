package com.aulms.model

import java.util.Objects
import com.aulms.model.PageMetadata
import com.aulms.model.TermCandidateSummary
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
 * 신규 용어 후보 목록 응답
 * @param items 
 * @param page 
 */
data class TermCandidateListResponse(

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("items", required = true) val items: kotlin.collections.List<TermCandidateSummary>,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("page", required = true) val page: PageMetadata
    ) {

}

