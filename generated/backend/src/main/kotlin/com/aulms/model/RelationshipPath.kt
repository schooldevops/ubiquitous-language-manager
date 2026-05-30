package com.aulms.model

import java.util.Objects
import com.aulms.model.GraphPathEdge
import com.aulms.model.GraphPathNode
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
 * Graphify 경로
 * @param nodes 
 * @param edges 
 */
data class RelationshipPath(

    @field:Valid
    @get:JsonProperty("nodes", required = true) val nodes: kotlin.collections.List<GraphPathNode>,

    @field:Valid
    @get:JsonProperty("edges", required = true) val edges: kotlin.collections.List<GraphPathEdge>
    ) {

}

