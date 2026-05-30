package com.aulms.model

import java.util.Objects
import com.aulms.model.ArtifactSourceType
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
 * PR 검증 대상 파일
 * @param filePath 변경 파일 경로
 * @param content 변경 파일 본문
 * @param sourceType 
 */
data class PullRequestArtifactFile(

    @get:JsonProperty("filePath", required = true) val filePath: kotlin.String,

    @get:Size(min=1)
    @get:JsonProperty("content", required = true) val content: kotlin.String,

    @field:Valid
    @get:JsonProperty("sourceType") val sourceType: ArtifactSourceType? = null
    ) {

}

