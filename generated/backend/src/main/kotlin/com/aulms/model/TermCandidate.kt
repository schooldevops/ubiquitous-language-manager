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
import io.swagger.v3.oas.annotations.media.Schema

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

    @Schema(example = "CAND-000001", required = true, description = "후보 식별자")
    @get:JsonProperty("candidateId", required = true) val candidateId: kotlin.String,

    @Schema(example = "고객선호배송시간대", required = true, description = "후보 한글 용어명")
    @get:JsonProperty("koreanName", required = true) val koreanName: kotlin.String,

    @Schema(example = "Customer Preferred Delivery Time Slot", required = true, description = "후보 영문 용어명")
    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @Schema(example = "CUST_PREF_DLV_TM_SLOT", required = true, description = "후보 영문 약어")
    @get:JsonProperty("englishAbbreviation", required = true) val englishAbbreviation: kotlin.String,

    @Schema(example = "고객", required = true, description = "업무 도메인명")
    @get:JsonProperty("domainName", required = true) val domainName: kotlin.String,

    @Schema(example = "고객이 선호하는 배송 시간대", required = true, description = "업무 정의")
    @get:JsonProperty("businessDefinition", required = true) val businessDefinition: kotlin.String,

    @Schema(example = "VARCHAR", required = true, description = "DB 물리 타입")
    @get:JsonProperty("physicalType", required = true) val physicalType: kotlin.String,

    @get:Min(0)
    @Schema(example = "20", required = true, description = "자릿수")
    @get:JsonProperty("digits", required = true) val digits: kotlin.Int,

    @get:Min(0)
    @Schema(example = "0", required = true, description = "소수점")
    @get:JsonProperty("decimalPoint", required = true) val decimalPoint: kotlin.Int,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("status", required = true) val status: CandidateStatus,

    @Schema(example = "planner01", required = true, description = "요청자")
    @get:JsonProperty("requestedBy", required = true) val requestedBy: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "등록 시 검색된 기존 유사 용어")
    @get:JsonProperty("similarTerms", required = true) val similarTerms: kotlin.collections.List<SimilarTerm>,

    @field:Valid
    @Schema(example = "null", required = true, description = "후보 상태 변경 이력")
    @get:JsonProperty("histories", required = true) val histories: kotlin.collections.List<CandidateHistory>,

    @Schema(example = "배송 옵션 추천과 배송 요청 화면에서 사용", description = "사용 맥락")
    @get:JsonProperty("usageContext") val usageContext: kotlin.String? = null,

    @Schema(example = "data.steward", description = "검토자")
    @get:JsonProperty("reviewedBy") val reviewedBy: kotlin.String? = null,

    @Schema(example = "T-000018", description = "승격된 표준 용어 ID")
    @get:JsonProperty("promotedTermId") val promotedTermId: kotlin.String? = null,

    @Schema(example = "null", description = "생성 일시")
    @get:JsonProperty("createdAt") val createdAt: java.time.OffsetDateTime? = null,

    @Schema(example = "null", description = "수정 일시")
    @get:JsonProperty("updatedAt") val updatedAt: java.time.OffsetDateTime? = null
    ) {

}

