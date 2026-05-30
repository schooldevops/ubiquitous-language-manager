package com.aulms.model

import java.util.Objects
import com.aulms.model.ArtifactSourceType
import com.aulms.model.ArtifactValidationSummary
import com.aulms.model.ValidationIssue
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
 * 개발 산출물 표준 용어 검증 결과
 * @param filePath 검증 대상 파일 경로
 * @param sourceType 
 * @param checkedCount 검증한 표현 수
 * @param summary 
 * @param issues 표준 위반 또는 권고 사항
 * @param exitCode CLI 호환 종료 코드. ERROR 또는 failOnWarning 조건이면 1.
 */
data class ArtifactValidationResult(

    @Schema(example = "openapi/customer.yaml", required = true, description = "검증 대상 파일 경로")
    @get:JsonProperty("filePath", required = true) val filePath: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("sourceType", required = true) val sourceType: ArtifactSourceType,

    @get:Min(0)
    @Schema(example = "1", required = true, description = "검증한 표현 수")
    @get:JsonProperty("checkedCount", required = true) val checkedCount: kotlin.Int,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("summary", required = true) val summary: ArtifactValidationSummary,

    @field:Valid
    @Schema(example = "null", required = true, description = "표준 위반 또는 권고 사항")
    @get:JsonProperty("issues", required = true) val issues: kotlin.collections.List<ValidationIssue>,

    @Schema(example = "1", required = true, description = "CLI 호환 종료 코드. ERROR 또는 failOnWarning 조건이면 1.")
    @get:JsonProperty("exitCode", required = true) val exitCode: kotlin.Int
    ) {

}

