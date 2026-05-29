package com.aulms.prompt

import com.aulms.model.PromptCandidateTerm
import com.aulms.model.PromptTemplate
import com.aulms.model.PromptTemplateListResponse
import com.aulms.model.PromptTemplatePreviewRequest
import com.aulms.model.PromptTemplatePreviewResponse
import com.aulms.model.PromptTemplateStatus
import com.aulms.model.PromptTemplateType
import com.aulms.model.PromptTemplateVersionListResponse
import com.aulms.model.PromptTermMapping
import org.springframework.stereotype.Service

@Service
class PromptTemplateService(private val repository: PromptTemplateRepository) {
    fun listPromptTemplates(type: PromptTemplateType?, status: PromptTemplateStatus?): PromptTemplateListResponse =
        repository.list(type, status)

    fun getPromptTemplate(templateId: String): PromptTemplate =
        repository.get(templateId)

    fun listPromptTemplateVersions(templateId: String): PromptTemplateVersionListResponse =
        repository.listVersions(templateId)

    fun previewPromptTemplate(templateId: String, request: PromptTemplatePreviewRequest): PromptTemplatePreviewResponse {
        val template = repository.get(templateId)
        val variables = mapOf(
            "requirementText" to request.requirementText,
            "termMappings" to renderTermMappings(request.termMappings.orEmpty()),
            "candidateTerms" to renderCandidateTerms(request.candidateTerms.orEmpty()),
            "additionalContext" to (request.additionalContext?.takeIf { it.isNotBlank() } ?: "없음"),
        )
        val rendered = variables.entries.fold(template.body) { body, (name, value) ->
            body.replace("{{$name}}", value)
        }
        return PromptTemplatePreviewResponse(
            templateId = template.templateId,
            version = template.version,
            renderedPrompt = rendered,
            injectedVariables = variables.filterValues { it.isNotBlank() }.keys.toList(),
        )
    }

    private fun renderTermMappings(mappings: List<PromptTermMapping>): String {
        if (mappings.isEmpty()) {
            return "데이터 사전 매핑 없음. 업무 개념 추출 후 표준 용어 검색 필요."
        }
        val rows = mappings.joinToString("\n") {
            "| ${it.concept} | ${it.standardTerm} | ${it.englishName} | ${it.dbColumn} | ${it.apiField} | ${it.codeVariable} | ${it.status.value} |"
        }
        return """
            | 개념 | 표준 용어 | 영문명 | DB 컬럼 | API 필드 | 코드 변수 | 상태 |
            |---|---|---|---|---|---|---|
            $rows
        """.trimIndent()
    }

    private fun renderCandidateTerms(candidates: List<PromptCandidateTerm>): String {
        if (candidates.isEmpty()) {
            return "신규 용어 후보 없음."
        }
        val rows = candidates.joinToString("\n") {
            "| ${it.candidateTerm} | ${it.recommendedEnglishName ?: ""} | ${it.recommendedAbbreviation ?: ""} | ${it.reason} | ${if (it.approvalRequired) "필요" else "불필요"} |"
        }
        return """
            | 후보 용어 | 추천 영문명 | 추천 약어 | 사유 | 승인 필요 여부 |
            |---|---|---|---|---|
            $rows
        """.trimIndent()
    }
}
