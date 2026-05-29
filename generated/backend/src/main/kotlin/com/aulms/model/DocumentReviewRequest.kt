package com.aulms.model

import java.util.Objects
import com.aulms.model.DocumentReviewOptions
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
 * 기획서 본문 용어 검토 요청
 * @param documentText 검토할 기획서 본문
 * @param domainNames 검토 대상 도메인 필터
 * @param options 
 */
data class DocumentReviewRequest(

    @get:Size(min=1)
    @get:JsonProperty("documentText", required = true) val documentText: kotlin.String,

    @get:JsonProperty("domainNames") val domainNames: kotlin.collections.List<kotlin.String>? = null,

    @field:Valid
    @get:JsonProperty("options") val options: DocumentReviewOptions? = null
    ) {

}

