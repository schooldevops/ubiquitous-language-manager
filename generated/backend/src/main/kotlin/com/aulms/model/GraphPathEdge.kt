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
 * Graphify 경로 관계
 * @param edgeKey 
 * @param relationshipType 
 * @param fromNodeKey 
 * @param toNodeKey 
 */
data class GraphPathEdge(

    @Schema(example = "TERM_RELATIONSHIP:term:T-000001:usedWith:term:T-000004", required = true, description = "")
    @get:JsonProperty("edgeKey", required = true) val edgeKey: kotlin.String,

    @Schema(example = "usedWith", required = true, description = "")
    @get:JsonProperty("relationshipType", required = true) val relationshipType: kotlin.String,

    @Schema(example = "term:T-000001", required = true, description = "")
    @get:JsonProperty("fromNodeKey", required = true) val fromNodeKey: kotlin.String,

    @Schema(example = "term:T-000004", required = true, description = "")
    @get:JsonProperty("toNodeKey", required = true) val toNodeKey: kotlin.String
    ) {

}

