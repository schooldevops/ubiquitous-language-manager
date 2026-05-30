package com.aulms.model

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
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
 * 컬럼 사용 시스템 정보
 * @param systemCode 
 * @param systemName 
 * @param tableName 
 * @param columnName 
 * @param termId 
 * @param standardTerm 
 * @param apiFields 
 */
data class ColumnSystemUsage(

    @Schema(example = "DICTIONARY", required = true, description = "")
    @get:JsonProperty("systemCode", required = true) val systemCode: kotlin.String,

    @Schema(example = "데이터 사전", required = true, description = "")
    @get:JsonProperty("systemName", required = true) val systemName: kotlin.String,

    @Schema(example = "고객", required = true, description = "")
    @get:JsonProperty("tableName", required = true) val tableName: kotlin.String,

    @Schema(example = "CUST_NO", required = true, description = "")
    @get:JsonProperty("columnName", required = true) val columnName: kotlin.String,

    @Schema(example = "T-000001", required = true, description = "")
    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @Schema(example = "고객번호", required = true, description = "")
    @get:JsonProperty("standardTerm", required = true) val standardTerm: kotlin.String,

    @Schema(example = "[\"customerNumber\"]", required = true, description = "")
    @get:JsonProperty("apiFields", required = true) val apiFields: kotlin.collections.List<kotlin.String>
    ) {

}

