package com.aulms.model

import java.util.Objects
import com.aulms.model.ImpactChangeType
import com.aulms.model.ImpactRecommendation
import com.aulms.model.ImpactRiskLevel
import com.aulms.model.ImpactTarget
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
 * 용어 변경 영향도 분석 응답
 * @param termId 
 * @param standardTerm 
 * @param changeType 
 * @param includeTwoHop 
 * @param riskLevel 
 * @param riskScore 
 * @param impactedTargets 영향받는 시스템, DB, API, DTO, 문서, 테스트
 * @param recommendations 권장 조치 목록
 */
data class ImpactAnalysisResponse(

    @get:JsonProperty("termId", required = true) val termId: kotlin.String,

    @get:JsonProperty("standardTerm", required = true) val standardTerm: kotlin.String,

    @field:Valid
    @get:JsonProperty("changeType", required = true) val changeType: ImpactChangeType,

    @get:JsonProperty("includeTwoHop", required = true) val includeTwoHop: kotlin.Boolean,

    @field:Valid
    @get:JsonProperty("riskLevel", required = true) val riskLevel: ImpactRiskLevel,

    @get:Min(0)
    @get:Max(100)
    @get:JsonProperty("riskScore", required = true) val riskScore: kotlin.Int,

    @field:Valid
    @get:JsonProperty("impactedTargets", required = true) val impactedTargets: kotlin.collections.List<ImpactTarget>,

    @field:Valid
    @get:JsonProperty("recommendations", required = true) val recommendations: kotlin.collections.List<ImpactRecommendation>
    ) {

}

