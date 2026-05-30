package com.aulms.model

import java.util.Objects
import com.aulms.model.DeprecatedUsage
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
 * 폐기어와 금지어 사용 위치 응답
 * @param items 
 */
data class DeprecatedUsageResponse(

    @field:Valid
    @get:JsonProperty("items", required = true) val items: kotlin.collections.List<DeprecatedUsage>
    ) {

}

