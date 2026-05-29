package com.aulms.model

import java.util.Objects
import com.aulms.model.MatchedExpression
import com.aulms.model.Recommendation
import com.aulms.model.TermStatus
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
 * 표준 용어 검색 결과
 * @param termId 표준 용어 식별자
 * @param standardTerm 한글 표준 용어명
 * @param englishName 영문 표준 용어명
 * @param dbColumn 표준 DB 컬럼명
 * @param apiField 표준 API 필드명
 * @param status 
 * @param matchedExpressions 입력 검색어와 매칭된 표현
 * @param recommendations 표준 표현 사용 권고
 * @param codeVariable 표준 코드 변수명
 * @param domainName 업무 도메인명
 * @param businessDefinition 업무 정의
 * @param usageContext 사용 맥락
 * @param score 검색 적합도 점수
 */
data class SearchResult(

    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @get:JsonProperty("standardTerm", required = true) val standardTerm: kotlin.String,

    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @get:JsonProperty("dbColumn", required = true) val dbColumn: kotlin.String,

    @get:JsonProperty("apiField", required = true) val apiField: kotlin.String,

    @field:Valid
    @get:JsonProperty("status", required = true) val status: TermStatus,

    @field:Valid
    @get:JsonProperty("matchedExpressions", required = true) val matchedExpressions: kotlin.collections.List<MatchedExpression>,

    @field:Valid
    @get:JsonProperty("recommendations", required = true) val recommendations: kotlin.collections.List<Recommendation>,

    @get:JsonProperty("codeVariable") val codeVariable: kotlin.String? = null,

    @get:JsonProperty("domainName") val domainName: kotlin.String? = null,

    @get:JsonProperty("businessDefinition") val businessDefinition: kotlin.String? = null,

    @get:JsonProperty("usageContext") val usageContext: kotlin.String? = null,

    @get:DecimalMin("0")
    @get:DecimalMax("1")
    @get:JsonProperty("score") val score: kotlin.Double? = null
    ) {

}

