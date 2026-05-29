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
 * 기획서 용어 검토 옵션
 * @param includeCandidateTerms 미매핑 표현을 신규 용어 후보로 반환할지 여부
 * @param includeValidationIssues Rule Engine 검증 결과를 포함할지 여부
 * @param normalizeSentences 표준화 문장 추천을 포함할지 여부
 */
data class DocumentReviewOptions(

    @get:JsonProperty("includeCandidateTerms") val includeCandidateTerms: kotlin.Boolean? = true,

    @get:JsonProperty("includeValidationIssues") val includeValidationIssues: kotlin.Boolean? = true,

    @get:JsonProperty("normalizeSentences") val normalizeSentences: kotlin.Boolean? = true
    ) {

}

