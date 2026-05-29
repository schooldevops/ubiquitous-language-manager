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

/**
 * 신규 용어 후보 목록 응답
 * @param items 
 * @param page 
 */
data class TermCandidateListResponse(

    @field:Valid
    @get:JsonProperty("items", required = true) val items: kotlin.collections.List<TermCandidateSummary>,

    @field:Valid
    @get:JsonProperty("page", required = true) val page: PageMetadata
    ) {

}

