package com.aulms.model

import java.util.Objects
import com.aulms.model.DomainTermStat
import com.aulms.model.RecentTermItem
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
 * 메인 대시보드 요약 데이터
 * @param totalTerms 전체 등록 용어 수
 * @param recentTerms 최근 등록 또는 수정된 용어 목록 (수정일 내림차순)
 * @param domainStats 도메인별 용어 통계
 */
data class DashboardSummary(

    @Schema(example = "9", required = true, description = "전체 등록 용어 수")
    @get:JsonProperty("totalTerms", required = true) val totalTerms: kotlin.Int,

    @field:Valid
    @Schema(example = "null", required = true, description = "최근 등록 또는 수정된 용어 목록 (수정일 내림차순)")
    @get:JsonProperty("recentTerms", required = true) val recentTerms: kotlin.collections.List<RecentTermItem>,

    @field:Valid
    @Schema(example = "null", required = true, description = "도메인별 용어 통계")
    @get:JsonProperty("domainStats", required = true) val domainStats: kotlin.collections.List<DomainTermStat>
    ) {

}

