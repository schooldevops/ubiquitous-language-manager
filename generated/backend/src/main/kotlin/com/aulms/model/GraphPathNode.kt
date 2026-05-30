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
 * Graphify 경로 노드
 * @param nodeKey 
 * @param nodeType 
 * @param label 
 */
data class GraphPathNode(

    @Schema(example = "term:T-000001", required = true, description = "")
    @get:JsonProperty("nodeKey", required = true) val nodeKey: kotlin.String,

    @Schema(example = "Term", required = true, description = "")
    @get:JsonProperty("nodeType", required = true) val nodeType: kotlin.String,

    @Schema(example = "고객번호", required = true, description = "")
    @get:JsonProperty("label", required = true) val label: kotlin.String
    ) {

}

