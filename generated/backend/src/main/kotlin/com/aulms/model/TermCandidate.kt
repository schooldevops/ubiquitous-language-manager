package com.aulms.model

import java.util.Objects
import com.aulms.model.CandidateHistory
import com.aulms.model.CandidateStatus
import com.aulms.model.SimilarTerm
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
 * 신규 용어 후보 상세 정보
 * @param candidateId 후보 식별자
 * @param koreanName 후보 한글 용어명
 * @param englishName 후보 영문 용어명
 * @param englishAbbreviation 후보 영문 약어
 * @param domainName 업무 도메인명
 * @param businessDefinition 업무 정의
 * @param physicalType DB 물리 타입
 * @param digits 자릿수
 * @param decimalPoint 소수점
 * @param status 
 * @param requestedBy 요청자
 * @param similarTerms 등록 시 검색된 기존 유사 용어
 * @param histories 후보 상태 변경 이력
 * @param usageContext 사용 맥락
 * @param reviewedBy 검토자
 * @param promotedTermId 승격된 표준 용어 ID
 * @param createdAt 생성 일시
 * @param updatedAt 수정 일시
 */
data class TermCandidate(

    @get:JsonProperty("candidateId", required = true) val candidateId: kotlin.String,

    @get:JsonProperty("koreanName", required = true) val koreanName: kotlin.String,

    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @get:JsonProperty("englishAbbreviation", required = true) val englishAbbreviation: kotlin.String,

    @get:JsonProperty("domainName", required = true) val domainName: kotlin.String,

    @get:JsonProperty("businessDefinition", required = true) val businessDefinition: kotlin.String,

    @get:JsonProperty("physicalType", required = true) val physicalType: kotlin.String,

    @get:Min(0)
    @get:JsonProperty("digits", required = true) val digits: kotlin.Int,

    @get:Min(0)
    @get:JsonProperty("decimalPoint", required = true) val decimalPoint: kotlin.Int,

    @field:Valid
    @get:JsonProperty("status", required = true) val status: CandidateStatus,

    @get:JsonProperty("requestedBy", required = true) val requestedBy: kotlin.String,

    @field:Valid
    @get:JsonProperty("similarTerms", required = true) val similarTerms: kotlin.collections.List<SimilarTerm>,

    @field:Valid
    @get:JsonProperty("histories", required = true) val histories: kotlin.collections.List<CandidateHistory>,

    @get:JsonProperty("usageContext") val usageContext: kotlin.String? = null,

    @get:JsonProperty("reviewedBy") val reviewedBy: kotlin.String? = null,

    @get:JsonProperty("promotedTermId") val promotedTermId: kotlin.String? = null,

    @get:JsonProperty("createdAt") val createdAt: java.time.OffsetDateTime? = null,

    @get:JsonProperty("updatedAt") val updatedAt: java.time.OffsetDateTime? = null
    ) {

}

