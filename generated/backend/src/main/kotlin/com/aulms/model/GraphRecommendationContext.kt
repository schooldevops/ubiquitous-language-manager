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
 * GraphDB 또는 Graphify 기반 관계 확장 결과
 * @param inferredDomainName 
 * @param relatedTerms 
 * @param relationshipHints 
 */
data class GraphRecommendationContext(

    @Schema(example = "고객", required = true, description = "")
    @get:JsonProperty("inferredDomainName", required = true) val inferredDomainName: kotlin.String,

    @Schema(example = "[\"고객번호\",\"고객명\",\"주문번호\"]", required = true, description = "")
    @get:JsonProperty("relatedTerms", required = true) val relatedTerms: kotlin.collections.List<kotlin.String>,

    @Schema(example = "[\"고객번호는 주문번호와 함께 주문 조회 조건에서 자주 사용됨\",\"고객 도메인 용어는 고객명, 고객상태코드와 함께 노출되는 경우가 많음\"]", required = true, description = "")
    @get:JsonProperty("relationshipHints", required = true) val relationshipHints: kotlin.collections.List<kotlin.String>
    ) {

}

