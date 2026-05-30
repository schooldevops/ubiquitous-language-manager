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
import io.swagger.v3.oas.annotations.media.Schema

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

    @Schema(example = "고객 ID를 입력하면 주문 리스트를 조회한다.", required = true, description = "원본 기획서 본문")
    @get:JsonProperty("originalText", required = true) val originalText: kotlin.String,

    @Schema(example = "고객번호를 입력하면 주문 목록을 조회한다.", required = true, description = "표준 용어로 치환한 추천 본문")
    @get:JsonProperty("recommendedText", required = true) val recommendedText: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "검출된 용어 표현 목록")
    @get:JsonProperty("detectedTerms", required = true) val detectedTerms: kotlin.collections.List<DetectedTerm>,

    @field:Valid
    @Schema(example = "null", required = true, description = "표준 용어 매핑표")
    @get:JsonProperty("standardMappings", required = true) val standardMappings: kotlin.collections.List<StandardMapping>,

    @field:Valid
    @Schema(example = "null", required = true, description = "데이터 사전에 매핑되지 않은 신규 용어 후보")
    @get:JsonProperty("candidateTerms", required = true) val candidateTerms: kotlin.collections.List<CandidateTerm>,

    @field:Valid
    @Schema(example = "null", required = true, description = "표준 위반 또는 권고 사항")
    @get:JsonProperty("validationIssues", required = true) val validationIssues: kotlin.collections.List<ValidationIssue>
    ) {

}

