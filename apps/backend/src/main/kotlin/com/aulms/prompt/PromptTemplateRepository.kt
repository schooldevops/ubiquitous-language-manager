package com.aulms.prompt

import com.aulms.model.PromptTemplate
import com.aulms.model.PromptTemplateHistory
import com.aulms.model.PromptTemplateListResponse
import com.aulms.model.PromptTemplateStatus
import com.aulms.model.PromptTemplateSummary
import com.aulms.model.PromptTemplateType
import com.aulms.model.PromptTemplateVariable
import com.aulms.model.PromptTemplateVersion
import com.aulms.model.PromptTemplateVersionListResponse
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Repository
class PromptTemplateRepository {
    private val baseTime = OffsetDateTime.of(2026, 5, 28, 9, 0, 0, 0, ZoneOffset.ofHours(9))
    private val templates = seedTemplates().associateBy { it.templateId }
    private val versions = templates.values.associate { template ->
        template.templateId to listOf(
            PromptTemplateVersion(
                templateId = template.templateId,
                version = template.version,
                status = template.status,
                changeReason = "MVP3 기본 ${template.type.value} 프롬프트 등록",
                createdBy = "system",
                createdAt = template.createdAt,
            ),
        )
    }

    fun list(type: PromptTemplateType?, status: PromptTemplateStatus?): PromptTemplateListResponse {
        val items = templates.values
            .filter { type == null || it.type == type }
            .filter { status == null || it.status == status }
            .sortedBy { it.templateId }
            .map { it.toSummary() }
        return PromptTemplateListResponse(items = items)
    }

    fun get(templateId: String): PromptTemplate =
        templates[templateId] ?: throw PromptTemplateNotFoundException(templateId)

    fun listVersions(templateId: String): PromptTemplateVersionListResponse {
        get(templateId)
        return PromptTemplateVersionListResponse(items = versions.getValue(templateId))
    }

    private fun PromptTemplate.toSummary(): PromptTemplateSummary =
        PromptTemplateSummary(
            templateId = templateId,
            type = type,
            name = name,
            version = version,
            status = status,
            description = description,
            updatedAt = updatedAt,
        )

    private fun seedTemplates(): List<PromptTemplate> =
        listOf(vibeCodingTemplate(), planningTemplate())

    private fun commonVariables(): List<PromptTemplateVariable> =
        listOf(
            PromptTemplateVariable(
                name = "requirementText",
                description = "사용자가 입력한 요구사항 또는 기획서 초안",
                required = true,
                source = PromptTemplateVariable.Source.UserInput,
            ),
            PromptTemplateVariable(
                name = "termMappings",
                description = "데이터 사전 검색 결과로 확정된 표준 용어 매핑 목록",
                required = false,
                source = PromptTemplateVariable.Source.DictionarySearch,
            ),
            PromptTemplateVariable(
                name = "candidateTerms",
                description = "데이터 사전에 없어 후보로 분리한 신규 용어 목록",
                required = false,
                source = PromptTemplateVariable.Source.CandidateWorkflow,
            ),
            PromptTemplateVariable(
                name = "additionalContext",
                description = "도구, 아키텍처, 산출물 범위 등 추가 작업 맥락",
                required = false,
                source = PromptTemplateVariable.Source.System,
            ),
        )

    private fun history(templateId: String, reason: String): List<PromptTemplateHistory> =
        listOf(
            PromptTemplateHistory(
                historyId = "PT-HIST-${templateId.substringAfterLast("-")}",
                templateId = templateId,
                version = "1.0.0",
                changeType = PromptTemplateHistory.ChangeType.Created,
                reason = reason,
                actor = "system",
                createdAt = baseTime,
            ),
        )

    private fun vibeCodingTemplate(): PromptTemplate =
        PromptTemplate(
            templateId = "PT-VIBE-001",
            type = PromptTemplateType.VibeCoding,
            name = "사내 데이터 사전 기반 개발 규칙",
            version = "1.0.0",
            status = PromptTemplateStatus.Active,
            description = "DB, API, DTO, Entity, 테스트 생성 시 표준 용어를 강제하는 프롬프트",
            body = """
                # 사내 데이터 사전 기반 개발 규칙

                너는 사내 표준 용어를 준수하는 개발 AI다.

                ## 요구사항

                {{requirementText}}

                ## 기본 원칙

                1. DB 컬럼명, API 필드명, DTO 변수명, Entity 속성명, 테스트 용어는 반드시 데이터 사전의 표준 용어를 사용한다.
                2. 데이터 사전에 없는 용어는 임의로 확정하지 않는다.
                3. 없는 용어는 신규 용어 후보로 분리한다.
                4. Approved 용어만 개발 산출물에 사용한다.
                5. Deprecated 용어는 사용하지 않고 대체 용어를 안내한다.
                6. 유사어가 입력되면 표준 용어로 변환한다.
                7. 서버 API는 반드시 OpenAPI Spec을 먼저 작성하고 generator 산출물을 기준으로 구현한다.

                ## 데이터 사전 매핑

                {{termMappings}}

                ## 신규 용어 후보

                {{candidateTerms}}

                ## 추가 맥락

                {{additionalContext}}

                ## 출력 형식

                ### 1. 업무 개념 추출
                ### 2. 데이터 사전 매핑
                ### 3. 생성 산출물
                ### 4. 신규 용어 후보
                ### 5. 표준 위반 경고
            """.trimIndent(),
            variables = commonVariables(),
            versionPolicy = "MAJOR는 출력 구조 변경, MINOR는 규칙 추가, PATCH는 문구 보완에 사용한다.",
            histories = history("PT-VIBE-001", "MVP3 기본 바이브코딩 템플릿 등록"),
            createdAt = baseTime,
            updatedAt = baseTime,
        )

    private fun planningTemplate(): PromptTemplate =
        PromptTemplate(
            templateId = "PT-PLAN-001",
            type = PromptTemplateType.Planning,
            name = "데이터 사전 기반 기획서 작성 규칙",
            version = "1.0.0",
            status = PromptTemplateStatus.Active,
            description = "요구사항과 기획서 표현을 표준 용어로 정규화하는 프롬프트",
            body = """
                # 데이터 사전 기반 기획서 작성 규칙

                아래 요구사항을 기획서 형식으로 작성하되, 모든 업무 용어는 사내 데이터 사전을 기준으로 표준화한다.

                ## 요구사항

                {{requirementText}}

                ## 작성 규칙

                1. 비표준 용어는 표준 용어로 변경한다.
                2. 모호한 용어는 후보를 제시한다.
                3. 데이터 사전에 없는 용어는 신규 용어 후보로 표시한다.
                4. 주요 용어는 문서 하단에 용어 매핑표로 정리한다.
                5. API, DB, 화면 표시명으로 이어질 수 있게 작성한다.

                ## 데이터 사전 매핑

                {{termMappings}}

                ## 신규 용어 후보

                {{candidateTerms}}

                ## 추가 맥락

                {{additionalContext}}

                ## 출력 형식

                ### 1. 기능 개요
                ### 2. 사용자 시나리오
                ### 3. 상세 요구사항
                ### 4. 예외사항
                ### 5. 사용 용어
                ### 6. 신규 용어 후보
            """.trimIndent(),
            variables = commonVariables(),
            versionPolicy = "MAJOR는 문서 구조 변경, MINOR는 작성 규칙 추가, PATCH는 문구 보완에 사용한다.",
            histories = history("PT-PLAN-001", "MVP3 기본 기획서 작성 템플릿 등록"),
            createdAt = baseTime,
            updatedAt = baseTime,
        )
}
