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

/**
 * 유사어, 금지어, 폐기어, 문맥 확인 표현
 * @param aliasId 
 * @param termId 
 * @param aliasName 
 * @param aliasType 
 * @param recommendationAction 
 * @param reason 
 */
data class TermAlias(

    @get:JsonProperty("aliasId", required = true) val aliasId: kotlin.String,

    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @get:JsonProperty("aliasName", required = true) val aliasName: kotlin.String,

    @field:Valid
    @get:JsonProperty("aliasType", required = true) val aliasType: AliasType,

    @get:JsonProperty("recommendationAction", required = true) val recommendationAction: kotlin.String,

    @get:JsonProperty("reason", required = true) val reason: kotlin.String
    ) {

}

