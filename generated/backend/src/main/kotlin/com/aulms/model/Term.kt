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

    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @get:JsonProperty("termNumber", required = true) val termNumber: kotlin.String,

    @get:JsonProperty("domainName", required = true) val domainName: kotlin.String,

    @get:JsonProperty("usageType", required = true) val usageType: kotlin.String,

    @get:JsonProperty("koreanName", required = true) val koreanName: kotlin.String,

    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @get:JsonProperty("englishAbbreviation", required = true) val englishAbbreviation: kotlin.String,

    @get:JsonProperty("businessDefinition", required = true) val businessDefinition: kotlin.String,

    @get:JsonProperty("physicalType", required = true) val physicalType: kotlin.String,

    @get:Min(0)
    @get:JsonProperty("digits", required = true) val digits: kotlin.Int,

    @get:Min(0)
    @get:JsonProperty("decimalPoint", required = true) val decimalPoint: kotlin.Int,

    @field:Valid
    @get:JsonProperty("status", required = true) val status: TermStatus,

    @get:JsonProperty("owner", required = true) val owner: kotlin.String,

    @get:JsonProperty("version", required = true) val version: kotlin.String,

    @field:Valid
    @get:JsonProperty("expressions", required = true) val expressions: kotlin.collections.List<TermExpression>,

    @field:Valid
    @get:JsonProperty("aliases", required = true) val aliases: kotlin.collections.List<TermAlias>,

    @get:JsonProperty("usageContext") val usageContext: kotlin.String? = null,

    @get:JsonProperty("createdAt") val createdAt: java.time.OffsetDateTime? = null,

    @get:JsonProperty("updatedAt") val updatedAt: java.time.OffsetDateTime? = null
    ) {

}

