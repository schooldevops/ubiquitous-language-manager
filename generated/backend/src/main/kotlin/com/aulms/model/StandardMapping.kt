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
 * 기획서 표현과 표준 용어 산출물 표현 매핑
 * @param inputExpression 기획서 입력 표현
 * @param termId 표준 용어 ID
 * @param standardTerm 한글 표준 용어명
 * @param englishName 영문 표준 용어명
 * @param dbColumn 표준 DB 컬럼명
 * @param apiField 표준 API 필드명
 * @param reason 표준 매핑 또는 권고 사유
 * @param codeVariable 표준 코드 변수명
 */
data class StandardMapping(

    @get:JsonProperty("inputExpression", required = true) val inputExpression: kotlin.String,

    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @get:JsonProperty("standardTerm", required = true) val standardTerm: kotlin.String,

    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @get:JsonProperty("dbColumn", required = true) val dbColumn: kotlin.String,

    @get:JsonProperty("apiField", required = true) val apiField: kotlin.String,

    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @get:JsonProperty("codeVariable") val codeVariable: kotlin.String? = null
    ) {

}

