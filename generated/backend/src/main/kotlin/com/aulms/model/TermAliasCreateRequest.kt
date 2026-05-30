package com.aulms.model

import java.util.Objects
import com.aulms.model.AliasType
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
 * 용어 별칭 등록 요청
 * @param aliasName 
 * @param aliasType 
 * @param recommendationAction 
 * @param reason 
 */
data class TermAliasCreateRequest(

    @get:Size(min=1,max=500)
    @Schema(example = "고객ID", required = true, description = "")
    @get:JsonProperty("aliasName", required = true) val aliasName: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("aliasType", required = true) val aliasType: AliasType,

    @get:Size(min=1,max=500)
    @Schema(example = "고객번호로 변환 권장", required = true, description = "")
    @get:JsonProperty("recommendationAction", required = true) val recommendationAction: kotlin.String,

    @get:Size(min=1)
    @Schema(example = "업무 고객 식별 번호 의미로 사용되는 경우 표준 용어는 고객번호", required = true, description = "")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String
    ) {

}

