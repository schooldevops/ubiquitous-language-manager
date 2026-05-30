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
 * 표준 용어 등록 요청
 * @param domainName 
 * @param usageType 
 * @param koreanName 
 * @param englishName 
 * @param englishAbbreviation 
 * @param businessDefinition 
 * @param physicalType 
 * @param digits 
 * @param decimalPoint 
 * @param owner 
 * @param usageContext 
 * @param status 
 */
data class TermCreateRequest(

    @get:Size(min=1,max=100)
    @Schema(example = "고객", required = true, description = "")
    @get:JsonProperty("domainName", required = true) val domainName: kotlin.String,

    @get:Size(min=1,max=50)
    @Schema(example = "표준항목", required = true, description = "")
    @get:JsonProperty("usageType", required = true) val usageType: kotlin.String,

    @get:Size(min=1,max=200)
    @Schema(example = "고객번호", required = true, description = "")
    @get:JsonProperty("koreanName", required = true) val koreanName: kotlin.String,

    @get:Size(min=1,max=300)
    @Schema(example = "Customer Number", required = true, description = "")
    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @get:Pattern(regexp="^[A-Z][A-Z0-9_]*$")
    @get:Size(min=1,max=100)
    @Schema(example = "CUST_NO", required = true, description = "")
    @get:JsonProperty("englishAbbreviation", required = true) val englishAbbreviation: kotlin.String,

    @get:Size(min=1)
    @Schema(example = "회사에서 고객을 업무적으로 식별하기 위해 사용하는 번호", required = true, description = "")
    @get:JsonProperty("businessDefinition", required = true) val businessDefinition: kotlin.String,

    @get:Size(min=1,max=50)
    @Schema(example = "VARCHAR", required = true, description = "")
    @get:JsonProperty("physicalType", required = true) val physicalType: kotlin.String,

    @get:Min(0)
    @Schema(example = "20", required = true, description = "")
    @get:JsonProperty("digits", required = true) val digits: kotlin.Int,

    @get:Min(0)
    @Schema(example = "0", required = true, description = "")
    @get:JsonProperty("decimalPoint", required = true) val decimalPoint: kotlin.Int,

    @get:Size(min=1,max=200)
    @Schema(example = "고객도메인 데이터스튜어드", required = true, description = "")
    @get:JsonProperty("owner", required = true) val owner: kotlin.String,

    @Schema(example = "주문, 계약, 청구, 상담 등에서 고객 식별 기준으로 사용", description = "")
    @get:JsonProperty("usageContext") val usageContext: kotlin.String? = null,

    @field:Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("status") val status: TermStatus? = null
    ) {

}

