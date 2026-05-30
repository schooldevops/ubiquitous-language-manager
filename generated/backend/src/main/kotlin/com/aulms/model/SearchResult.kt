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
import io.swagger.v3.oas.annotations.media.Schema

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

    @Schema(example = "T-000001", required = true, description = "표준 용어 식별자")
    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @Schema(example = "고객번호", required = true, description = "한글 표준 용어명")
    @get:JsonProperty("standardTerm", required = true) val standardTerm: kotlin.String,

    @Schema(example = "Customer Number", required = true, description = "영문 표준 용어명")
    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @Schema(example = "CUST_NO", required = true, description = "표준 DB 컬럼명")
    @get:JsonProperty("dbColumn", required = true) val dbColumn: kotlin.String,

    @Schema(example = "customerNumber", required = true, description = "표준 API 필드명")
    @get:JsonProperty("apiField", required = true) val apiField: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("status", required = true) val status: TermStatus,

    @field:Valid
    @Schema(example = "null", required = true, description = "입력 검색어와 매칭된 표현")
    @get:JsonProperty("matchedExpressions", required = true) val matchedExpressions: kotlin.collections.List<MatchedExpression>,

    @field:Valid
    @Schema(example = "null", required = true, description = "표준 표현 사용 권고")
    @get:JsonProperty("recommendations", required = true) val recommendations: kotlin.collections.List<Recommendation>,

    @Schema(example = "customerNumber", description = "표준 코드 변수명")
    @get:JsonProperty("codeVariable") val codeVariable: kotlin.String? = null,

    @Schema(example = "고객", description = "업무 도메인명")
    @get:JsonProperty("domainName") val domainName: kotlin.String? = null,

    @Schema(example = "회사에서 고객을 업무적으로 식별하기 위해 사용하는 번호", description = "업무 정의")
    @get:JsonProperty("businessDefinition") val businessDefinition: kotlin.String? = null,

    @Schema(example = "주문, 계약, 청구, 상담 등에서 고객 식별 기준으로 사용", description = "사용 맥락")
    @get:JsonProperty("usageContext") val usageContext: kotlin.String? = null,

    @get:DecimalMin("0")
    @get:DecimalMax("1")
    @Schema(example = "0.98", description = "검색 적합도 점수")
    @get:JsonProperty("score") val score: kotlin.Double? = null
    ) {

}

