package com.aulms.model

import java.util.Objects
import com.aulms.model.Recommendation
import com.aulms.model.SearchResult
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
 * 폐기어 검색 결과와 대체 표준 용어
 * @param deprecatedExpression 폐기되었거나 사용 금지된 표현
 * @param reason 폐기 또는 사용 금지 사유
 * @param replacementTerm 
 * @param recommendation 
 */
data class DeprecatedSearchResult(

    @Schema(example = "CUST_ID", required = true, description = "폐기되었거나 사용 금지된 표현")
    @get:JsonProperty("deprecatedExpression", required = true) val deprecatedExpression: kotlin.String,

    @Schema(example = "기술 식별자와 업무 고객번호가 혼동될 수 있음", required = true, description = "폐기 또는 사용 금지 사유")
    @get:JsonProperty("reason", required = true) val reason: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("replacementTerm", required = true) val replacementTerm: SearchResult,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("recommendation", required = true) val recommendation: Recommendation
    ) {

}

