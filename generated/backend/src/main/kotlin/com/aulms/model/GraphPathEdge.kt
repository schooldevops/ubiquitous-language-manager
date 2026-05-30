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

/**
 * Graphify 경로 관계
 * @param edgeKey 
 * @param relationshipType 
 * @param fromNodeKey 
 * @param toNodeKey 
 */
data class GraphPathEdge(

    @get:JsonProperty("edgeKey", required = true) val edgeKey: kotlin.String,

    @get:JsonProperty("relationshipType", required = true) val relationshipType: kotlin.String,

    @get:JsonProperty("fromNodeKey", required = true) val fromNodeKey: kotlin.String,

    @get:JsonProperty("toNodeKey", required = true) val toNodeKey: kotlin.String
    ) {

}

