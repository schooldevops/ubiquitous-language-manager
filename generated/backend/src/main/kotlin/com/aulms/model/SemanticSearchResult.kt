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
import io.swagger.v3.oas.annotations.media.Schema

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

    @Schema(example = "T-000005", required = true, description = "표준 용어 ID")
    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @Schema(example = "주문일자", required = true, description = "한글 표준 용어명")
    @get:JsonProperty("standardTerm", required = true) val standardTerm: kotlin.String,

    @Schema(example = "Order Date", required = true, description = "영문 표준 용어명")
    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @Schema(example = "ORD_DT", required = true, description = "표준 DB 컬럼명")
    @get:JsonProperty("dbColumn", required = true) val dbColumn: kotlin.String,

    @Schema(example = "orderDate", required = true, description = "표준 API 필드명")
    @get:JsonProperty("apiField", required = true) val apiField: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("status", required = true) val status: TermStatus,

    @get:DecimalMin("0")
    @get:DecimalMax("1")
    @Schema(example = "0.92", required = true, description = "의미 유사도 점수")
    @get:JsonProperty("similarityScore", required = true) val similarityScore: kotlin.Double,

    @Schema(example = "질의의 주문, 발생, 날짜 의미가 주문일자의 업무 정의와 가장 유사함", required = true, description = "후보 추천 사유")
    @get:JsonProperty("recommendationReason", required = true) val recommendationReason: kotlin.String,

    @Schema(example = "주문일자는 날짜만 표현하고 주문일시는 시분초까지 포함함", required = true, description = "유사 후보 간 차이 설명")
    @get:JsonProperty("differenceDescription", required = true) val differenceDescription: kotlin.String,

    @Schema(example = "Approved 용어이므로 기획서와 개발 산출물에 사용할 수 있음", description = "상태별 사용 안내")
    @get:JsonProperty("guidance") val guidance: kotlin.String? = null,

    @field:Valid
    @Schema(example = "null", description = "Rule Engine 후처리 결과")
    @get:JsonProperty("validationIssues") val validationIssues: kotlin.collections.List<ValidationIssue>? = null
    ) {

}

