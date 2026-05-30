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

    @get:JsonProperty("systemCode", required = true) val systemCode: kotlin.String,

    @get:JsonProperty("systemName", required = true) val systemName: kotlin.String,

    @get:JsonProperty("tableName", required = true) val tableName: kotlin.String,

    @get:JsonProperty("columnName", required = true) val columnName: kotlin.String,

    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @get:JsonProperty("standardTerm", required = true) val standardTerm: kotlin.String,

    @get:JsonProperty("apiFields", required = true) val apiFields: kotlin.collections.List<kotlin.String>
    ) {

}

