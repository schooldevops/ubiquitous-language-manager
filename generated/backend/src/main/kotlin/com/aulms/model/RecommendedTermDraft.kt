package com.aulms.model

import java.util.Objects
import com.aulms.model.RecommendedAlias
import com.aulms.model.RecommendedExpression
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
 * 추천으로 채워줄 용어 초안
 * @param domainName 
 * @param usageType 
 * @param englishName 
 * @param englishAbbreviation 
 * @param businessDefinition 
 * @param usageContext 
 * @param physicalType 
 * @param digits 
 * @param decimalPoint 
 * @param owner 
 * @param expressions 산출물별 표현 추천 (DB 컬럼, API 필드, 코드 변수, UI 라벨 등)
 * @param aliases 별칭/유사어/금지어 추천 목록
 */
data class RecommendedTermDraft(

    @Schema(example = "고객", required = true, description = "")
    @get:JsonProperty("domainName", required = true) val domainName: kotlin.String,

    @Schema(example = "표준항목", required = true, description = "")
    @get:JsonProperty("usageType", required = true) val usageType: kotlin.String,

    @Schema(example = "Customer Preferred Delivery Time Slot", required = true, description = "")
    @get:JsonProperty("englishName", required = true) val englishName: kotlin.String,

    @Schema(example = "CUST_PREF_DLV_TM_SLOT", required = true, description = "")
    @get:JsonProperty("englishAbbreviation", required = true) val englishAbbreviation: kotlin.String,

    @Schema(example = "고객 도메인에서 고객이 선호하는 배송 시간대를 관리하기 위해 사용하는 표준 항목", required = true, description = "")
    @get:JsonProperty("businessDefinition", required = true) val businessDefinition: kotlin.String,

    @Schema(example = "배송 옵션 추천, 배송 요청 화면, 배송 정책 API에서 고객별 선호 시간대 기준으로 사용", required = true, description = "")
    @get:JsonProperty("usageContext", required = true) val usageContext: kotlin.String,

    @Schema(example = "VARCHAR", required = true, description = "")
    @get:JsonProperty("physicalType", required = true) val physicalType: kotlin.String,

    @Schema(example = "40", required = true, description = "")
    @get:JsonProperty("digits", required = true) val digits: kotlin.Int,

    @Schema(example = "0", required = true, description = "")
    @get:JsonProperty("decimalPoint", required = true) val decimalPoint: kotlin.Int,

    @Schema(example = "고객도메인 데이터스튜어드", required = true, description = "")
    @get:JsonProperty("owner", required = true) val owner: kotlin.String,

    @field:Valid
    @Schema(example = "[{\"expressionType\":\"DB_COLUMN\",\"expressionValue\":\"CUST_NO\",\"isStandard\":true},{\"expressionType\":\"API_FIELD\",\"expressionValue\":\"customerNumber\",\"isStandard\":true}]", required = true, description = "산출물별 표현 추천 (DB 컬럼, API 필드, 코드 변수, UI 라벨 등)")
    @get:JsonProperty("expressions", required = true) val expressions: kotlin.collections.List<RecommendedExpression>,

    @field:Valid
    @Schema(example = "[{\"aliasName\":\"고객ID\",\"aliasType\":\"Forbidden\",\"recommendationAction\":\"고객번호로 변환 권장\",\"reason\":\"기술 ID와 혼동되어 금지어로 분류\"}]", required = true, description = "별칭/유사어/금지어 추천 목록")
    @get:JsonProperty("aliases", required = true) val aliases: kotlin.collections.List<RecommendedAlias>
    ) {

}

