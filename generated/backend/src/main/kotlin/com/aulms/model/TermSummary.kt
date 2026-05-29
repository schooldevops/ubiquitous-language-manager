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

    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @get:JsonProperty("termNumber", required = true) val termNumber: kotlin.String,

    @get:JsonProperty("domainName", required = true) val domainName: kotlin.String,

    @get:JsonProperty("koreanName", required = true) val koreanName: kotlin.String,

    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @get:JsonProperty("englishAbbreviation", required = true) val englishAbbreviation: kotlin.String,

    @field:Valid
    @get:JsonProperty("status", required = true) val status: TermStatus,

    @get:JsonProperty("apiFieldName") val apiFieldName: kotlin.String? = null,

    @get:JsonProperty("relatedSystems") val relatedSystems: kotlin.collections.List<kotlin.String>? = null
    ) {

}

