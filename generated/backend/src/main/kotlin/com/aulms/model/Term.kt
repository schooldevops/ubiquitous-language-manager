package com.aulms.model

import java.util.Objects
import com.aulms.model.TermAlias
import com.aulms.model.TermExpression
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
 * 표준 용어 상세 정보
 * @param termId 용어를 식별하는 내부 ID
 * @param termNumber 업무적으로 노출 가능한 용어 번호
 * @param domainName 용어가 속한 업무 도메인명
 * @param usageType 용어 사용 구분
 * @param koreanName 한글 표준 용어명
 * @param englishName 영문 표준 용어명
 * @param englishAbbreviation 영문 약어 또는 DB 컬럼명 후보
 * @param businessDefinition 업무 정의
 * @param physicalType DB 물리 타입
 * @param digits 데이터 길이
 * @param decimalPoint 소수점 자리수
 * @param status 
 * @param owner 용어 소유자
 * @param version 용어 버전
 * @param expressions 산출물별 표현 매핑
 * @param aliases 유사어, 금지어, 폐기어 목록
 * @param usageContext 사용 맥락
 * @param createdAt 생성 일시
 * @param updatedAt 수정 일시
 */
data class Term(

    @Schema(example = "T-000001", required = true, description = "용어를 식별하는 내부 ID")
    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @Schema(example = "TERM-000001", required = true, description = "업무적으로 노출 가능한 용어 번호")
    @get:JsonProperty("termNumber", required = true) val termNumber: kotlin.String,

    @Schema(example = "고객", required = true, description = "용어가 속한 업무 도메인명")
    @get:JsonProperty("domainName", required = true) val domainName: kotlin.String,

    @Schema(example = "표준항목", required = true, description = "용어 사용 구분")
    @get:JsonProperty("usageType", required = true) val usageType: kotlin.String,

    @Schema(example = "고객번호", required = true, description = "한글 표준 용어명")
    @get:JsonProperty("koreanName", required = true) val koreanName: kotlin.String,

    @Schema(example = "Customer Number", required = true, description = "영문 표준 용어명")
    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @Schema(example = "CUST_NO", required = true, description = "영문 약어 또는 DB 컬럼명 후보")
    @get:JsonProperty("englishAbbreviation", required = true) val englishAbbreviation: kotlin.String,

    @Schema(example = "회사에서 고객을 업무적으로 식별하기 위해 사용하는 번호", required = true, description = "업무 정의")
    @get:JsonProperty("businessDefinition", required = true) val businessDefinition: kotlin.String,

    @Schema(example = "VARCHAR", required = true, description = "DB 물리 타입")
    @get:JsonProperty("physicalType", required = true) val physicalType: kotlin.String,

    @get:Min(0)
    @Schema(example = "20", required = true, description = "데이터 길이")
    @get:JsonProperty("digits", required = true) val digits: kotlin.Int,

    @get:Min(0)
    @Schema(example = "0", required = true, description = "소수점 자리수")
    @get:JsonProperty("decimalPoint", required = true) val decimalPoint: kotlin.Int,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("status", required = true) val status: TermStatus,

    @Schema(example = "고객도메인 데이터스튜어드", required = true, description = "용어 소유자")
    @get:JsonProperty("owner", required = true) val owner: kotlin.String,

    @Schema(example = "1.0", required = true, description = "용어 버전")
    @get:JsonProperty("version", required = true) val version: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "산출물별 표현 매핑")
    @get:JsonProperty("expressions", required = true) val expressions: kotlin.collections.List<TermExpression>,

    @field:Valid
    @Schema(example = "null", required = true, description = "유사어, 금지어, 폐기어 목록")
    @get:JsonProperty("aliases", required = true) val aliases: kotlin.collections.List<TermAlias>,

    @Schema(example = "주문, 계약, 청구, 상담 등에서 고객 식별 기준으로 사용", description = "사용 맥락")
    @get:JsonProperty("usageContext") val usageContext: kotlin.String? = null,

    @Schema(example = "null", description = "생성 일시")
    @get:JsonProperty("createdAt") val createdAt: java.time.OffsetDateTime? = null,

    @Schema(example = "null", description = "수정 일시")
    @get:JsonProperty("updatedAt") val updatedAt: java.time.OffsetDateTime? = null
    ) {

}

