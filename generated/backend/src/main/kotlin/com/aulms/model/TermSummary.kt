package com.aulms.model

import java.util.Objects
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
 * 표준 용어 목록 조회용 요약 정보
 * @param termId 
 * @param termNumber 
 * @param domainName 
 * @param koreanName 
 * @param englishName 
 * @param englishAbbreviation 
 * @param status 
 * @param apiFieldName 표준 API 필드명
 * @param relatedSystems 관련 시스템 목록
 */
data class TermSummary(

    @Schema(example = "T-000001", required = true, description = "")
    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @Schema(example = "TERM-000001", required = true, description = "")
    @get:JsonProperty("termNumber", required = true) val termNumber: kotlin.String,

    @Schema(example = "고객", required = true, description = "")
    @get:JsonProperty("domainName", required = true) val domainName: kotlin.String,

    @Schema(example = "고객번호", required = true, description = "")
    @get:JsonProperty("koreanName", required = true) val koreanName: kotlin.String,

    @Schema(example = "Customer Number", required = true, description = "")
    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @Schema(example = "CUST_NO", required = true, description = "")
    @get:JsonProperty("englishAbbreviation", required = true) val englishAbbreviation: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("status", required = true) val status: TermStatus,

    @Schema(example = "customerNumber", description = "표준 API 필드명")
    @get:JsonProperty("apiFieldName") val apiFieldName: kotlin.String? = null,

    @Schema(example = "[\"CRM\",\"ORDER\"]", description = "관련 시스템 목록")
    @get:JsonProperty("relatedSystems") val relatedSystems: kotlin.collections.List<kotlin.String>? = null
    ) {

}

