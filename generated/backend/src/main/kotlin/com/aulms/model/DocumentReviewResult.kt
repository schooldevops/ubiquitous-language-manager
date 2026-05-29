package com.aulms.model

import java.util.Objects
import com.aulms.model.CandidateTerm
import com.aulms.model.DetectedTerm
import com.aulms.model.StandardMapping
import com.aulms.model.ValidationIssue
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
 * 기획서 본문 용어 검토 결과
 * @param originalText 원본 기획서 본문
 * @param recommendedText 표준 용어로 치환한 추천 본문
 * @param detectedTerms 검출된 용어 표현 목록
 * @param standardMappings 표준 용어 매핑표
 * @param candidateTerms 데이터 사전에 매핑되지 않은 신규 용어 후보
 * @param validationIssues 표준 위반 또는 권고 사항
 */
data class DocumentReviewResult(

    @get:JsonProperty("originalText", required = true) val originalText: kotlin.String,

    @get:JsonProperty("recommendedText", required = true) val recommendedText: kotlin.String,

    @field:Valid
    @get:JsonProperty("detectedTerms", required = true) val detectedTerms: kotlin.collections.List<DetectedTerm>,

    @field:Valid
    @get:JsonProperty("standardMappings", required = true) val standardMappings: kotlin.collections.List<StandardMapping>,

    @field:Valid
    @get:JsonProperty("candidateTerms", required = true) val candidateTerms: kotlin.collections.List<CandidateTerm>,

    @field:Valid
    @get:JsonProperty("validationIssues", required = true) val validationIssues: kotlin.collections.List<ValidationIssue>
    ) {

}

