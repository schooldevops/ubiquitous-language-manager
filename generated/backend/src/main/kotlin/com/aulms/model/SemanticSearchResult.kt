package com.aulms.model

import java.util.Objects
import com.aulms.model.TermStatus
import com.aulms.model.ValidationIssue
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
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
 * 의미 기반 검색 결과
 * @param termId 표준 용어 ID
 * @param standardTerm 한글 표준 용어명
 * @param englishName 영문 표준 용어명
 * @param dbColumn 표준 DB 컬럼명
 * @param apiField 표준 API 필드명
 * @param status 
 * @param similarityScore 의미 유사도 점수
 * @param recommendationReason 후보 추천 사유
 * @param differenceDescription 유사 후보 간 차이 설명
 * @param guidance 상태별 사용 안내
 * @param validationIssues Rule Engine 후처리 결과
 */
data class SemanticSearchResult(

    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @get:JsonProperty("standardTerm", required = true) val standardTerm: kotlin.String,

    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @get:JsonProperty("dbColumn", required = true) val dbColumn: kotlin.String,

    @get:JsonProperty("apiField", required = true) val apiField: kotlin.String,

    @field:Valid
    @get:JsonProperty("status", required = true) val status: TermStatus,

    @get:DecimalMin("0")
    @get:DecimalMax("1")
    @get:JsonProperty("similarityScore", required = true) val similarityScore: kotlin.Double,

    @get:JsonProperty("recommendationReason", required = true) val recommendationReason: kotlin.String,

    @get:JsonProperty("differenceDescription", required = true) val differenceDescription: kotlin.String,

    @get:JsonProperty("guidance") val guidance: kotlin.String? = null,

    @field:Valid
    @get:JsonProperty("validationIssues") val validationIssues: kotlin.collections.List<ValidationIssue>? = null
    ) {

}

