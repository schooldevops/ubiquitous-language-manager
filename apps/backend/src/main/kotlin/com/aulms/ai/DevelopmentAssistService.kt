package com.aulms.ai

import com.aulms.model.AssistCandidateTerm
import com.aulms.model.AssistTargetArtifact
import com.aulms.model.AssistTermMapping
import com.aulms.model.DevelopmentAssistRequest
import com.aulms.model.DevelopmentAssistResponse
import com.aulms.model.ExtractedBusinessConcept
import com.aulms.model.ExpressionType
import com.aulms.model.GeneratedArtifact
import com.aulms.model.SearchResult
import com.aulms.model.SemanticSearchRequest
import com.aulms.model.SemanticSearchResult
import com.aulms.model.StandardViolationWarning
import com.aulms.model.TermStatus
import com.aulms.rule.RuleEngine
import com.aulms.rule.RuleValidationRequest
import com.aulms.search.SearchService
import com.aulms.search.SemanticSearchService
import com.aulms.term.TermRepository
import com.aulms.term.TermSearchDocument
import org.springframework.stereotype.Service

@Service
class DevelopmentAssistService(
    private val termRepository: TermRepository,
    private val searchService: SearchService,
    private val semanticSearchService: SemanticSearchService,
    private val ruleEngine: RuleEngine,
) {
    fun developmentAssist(request: DevelopmentAssistRequest): DevelopmentAssistResponse {
        val extracted = extractConcepts(request.requirementText)
        val mappingResults = extracted.mapNotNull { concept ->
            resolveMapping(concept.concept, request.domainNames.orEmpty())
        }.distinctBy { it.termId }
        val mappedConcepts = mappingResults.map { it.concept }.toSet()
        val candidates = extractCandidates(request.requirementText, extracted, mappedConcepts)
        val warnings = collectWarnings(request.requirementText, mappingResults)
        val artifacts = generateArtifacts(request.targetArtifacts, mappingResults)

        return DevelopmentAssistResponse(
            requirementText = request.requirementText,
            extractedConcepts = extracted,
            termMappings = mappingResults,
            generatedArtifacts = artifacts,
            candidateTerms = candidates,
            warnings = warnings,
        )
    }

    private fun extractConcepts(requirementText: String): List<ExtractedBusinessConcept> {
        val concepts = linkedMapOf<String, ExtractedBusinessConcept>()
        fun add(concept: String, reason: String, confidence: Double) {
            concepts.putIfAbsent(concept, ExtractedBusinessConcept(concept = concept, reason = reason, confidence = confidence))
        }

        if (requirementText.contains("고객")) {
            add("고객번호", "고객별 조회 조건에서 고객 식별 기준이 필요함", 0.95)
        }
        if (requirementText.contains("고객ID", ignoreCase = true) || requirementText.contains("customerId", ignoreCase = true)) {
            add("고객ID", "입력 요구사항에 비표준 고객 식별 표현이 포함됨", 0.91)
        }
        if (requirementText.contains("주문")) {
            add("주문번호", "주문 내역 응답의 주문 식별자가 필요함", 0.93)
            if (requirementText.contains("내역") || requirementText.contains("목록") || requirementText.contains("조회") || requirementText.contains("API", ignoreCase = true)) {
                add("주문일자", "주문 내역 응답의 발생 날짜가 필요함", 0.9)
                add("주문금액", "주문 내역 응답의 금액 정보가 필요함", 0.9)
                add("주문상태코드", "주문 내역 응답의 처리 상태가 필요함", 0.88)
            }
        }
        if (requirementText.contains("배송") && requirementText.contains("시간대")) {
            add("고객선호배송시간대", "데이터 사전에 없을 수 있는 배송 시간대 업무 개념 후보", 0.75)
        }

        return concepts.values.toList()
    }

    private fun resolveMapping(concept: String, domainNames: List<String>): AssistTermMapping? {
        val exact = searchService.exactSearch(concept, TermStatus.Approved).items.firstOrNull()
        if (exact != null) {
            return exact.toMapping(concept, AssistTermMapping.MappingSource.Exact, "정확 검색으로 승인된 표준 용어를 확인함")
        }

        val alias = searchService.aliasSearch(concept).items.firstOrNull { it.status == TermStatus.Approved }
        if (alias != null) {
            return alias.toMapping(concept, AssistTermMapping.MappingSource.Alias, "유사어 검색으로 표준 용어를 확인함")
        }

        val semantic = semanticSearchService.semanticSearch(
            SemanticSearchRequest(
                query = concept,
                domainNames = domainNames.takeIf { it.isNotEmpty() },
                statuses = listOf(TermStatus.Approved),
                limit = 1,
            ),
        ).items.firstOrNull()
        return semantic?.toMapping(concept)
    }

    private fun extractCandidates(
        requirementText: String,
        extracted: List<ExtractedBusinessConcept>,
        mappedConcepts: Set<String>,
    ): List<AssistCandidateTerm> {
        val candidates = extracted
            .filterNot { it.concept in mappedConcepts }
            .filterNot { searchService.aliasSearch(it.concept).items.isNotEmpty() }
            .map { it.concept }
            .toMutableList()

        if (requirementText.contains("배송") && requirementText.contains("시간대") && "고객선호배송시간대" !in candidates) {
            candidates.add("고객선호배송시간대")
        }

        return candidates.distinct().map {
            AssistCandidateTerm(
                candidateTerm = it,
                recommendedEnglishName = when (it) {
                    "고객선호배송시간대" -> "Customer Preferred Delivery Time Slot"
                    else -> null
                },
                recommendedAbbreviation = when (it) {
                    "고객선호배송시간대" -> "CUST_PREF_DLV_TM_SLOT"
                    else -> null
                },
                reason = "데이터 사전에 일치하는 승인 용어가 없어 신규 용어 후보로 분리함",
                approvalRequired = true,
            )
        }
    }

    private fun collectWarnings(requirementText: String, mappings: List<AssistTermMapping>): List<StandardViolationWarning> {
        val inputWarnings = listOf("고객ID", "customerId", "CUST_ID")
            .filter { requirementText.contains(it, ignoreCase = true) }
            .flatMap { expression ->
                ruleEngine.validateExpression(
                    RuleValidationRequest(
                        source = "AI_DEVELOPMENT_ASSIST",
                        inputExpression = expression,
                        expressionType = if (expression.all { it.isUpperCase() || it == '_' || it.isDigit() }) {
                            ExpressionType.DB_COLUMN
                        } else {
                            ExpressionType.API_FIELD
                        },
                        developmentUsage = true,
                    ),
                ).map { it.toWarning() }
            }

        val mappingWarnings = mappings.flatMap {
            ruleEngine.validateExpression(
                RuleValidationRequest(
                    source = "AI_DEVELOPMENT_ASSIST",
                    inputExpression = it.apiField,
                    expressionType = ExpressionType.API_FIELD,
                    developmentUsage = true,
                ),
            ).map { issue -> issue.toWarning() }
        }

        return (inputWarnings + mappingWarnings).distinctBy { "${it.inputExpression}:${it.reason}" }
    }

    private fun generateArtifacts(targetArtifacts: List<AssistTargetArtifact>, mappings: List<AssistTermMapping>): List<GeneratedArtifact> =
        targetArtifacts.map { artifactType ->
            when (artifactType) {
                AssistTargetArtifact.DTO -> GeneratedArtifact(artifactType, "CustomerOrderResponse", dtoContent(mappings))
                AssistTargetArtifact.OPENAPI_SCHEMA -> GeneratedArtifact(artifactType, "CustomerOrderResponse", openApiSchemaContent(mappings))
                AssistTargetArtifact.SQL_EXAMPLE -> GeneratedArtifact(artifactType, "CustomerOrderSearchSql", sqlContent(mappings))
            }
        }

    private fun dtoContent(mappings: List<AssistTermMapping>): String {
        val properties = mappings.joinToString("\n") { "    val ${it.codeVariable}: ${kotlinType(it)}," }
        return """
            data class CustomerOrderResponse(
            $properties
            )
        """.trimIndent()
    }

    private fun openApiSchemaContent(mappings: List<AssistTermMapping>): String {
        val properties = mappings.joinToString("\n") {
            val (type, format) = openApiType(it)
            val formatLine = format?.let { value -> "\n        format: $value" } ?: ""
            """
                ${it.apiField}:
                  type: $type$formatLine
                  description: ${it.standardTerm}
            """.trimIndent()
        }.prependIndent("    ")
        return """
            CustomerOrderResponse:
              type: object
              properties:
            $properties
        """.trimIndent()
    }

    private fun sqlContent(mappings: List<AssistTermMapping>): String {
        val columns = mappings.joinToString(",\n    ") { it.dbColumn }
        val customer = mappings.firstOrNull { it.standardTerm == "고객번호" }
        val where = customer?.let { "\nWHERE ${it.dbColumn} = :${it.apiField}" } ?: ""
        return """
            SELECT
                $columns
            FROM ORDER_HISTORY$where;
        """.trimIndent()
    }

    private fun kotlinType(mapping: AssistTermMapping): String {
        val document = document(mapping.termId)
        return when (document?.term?.physicalType) {
            "DATE" -> "LocalDate"
            "TIMESTAMP" -> "LocalDateTime"
            "NUMERIC", "NUMBER", "DECIMAL" -> "BigDecimal"
            else -> "String"
        }
    }

    private fun openApiType(mapping: AssistTermMapping): Pair<String, String?> {
        val document = document(mapping.termId)
        return when (document?.term?.physicalType) {
            "DATE" -> "string" to "date"
            "TIMESTAMP" -> "string" to "date-time"
            "NUMERIC", "NUMBER", "DECIMAL" -> "number" to null
            else -> "string" to null
        }
    }

    private fun document(termId: String): TermSearchDocument? =
        termRepository.searchDocuments().firstOrNull { it.term.termId == termId }

    private fun SearchResult.toMapping(concept: String, source: AssistTermMapping.MappingSource, reason: String): AssistTermMapping =
        AssistTermMapping(
            concept = concept,
            termId = termId,
            standardTerm = standardTerm,
            englishName = englishName,
            dbColumn = dbColumn,
            apiField = apiField,
            codeVariable = codeVariable ?: apiField,
            status = status,
            mappingSource = source,
            recommendationReason = reason,
        )

    private fun SemanticSearchResult.toMapping(concept: String): AssistTermMapping =
        AssistTermMapping(
            concept = concept,
            termId = termId,
            standardTerm = standardTerm,
            englishName = englishName,
            dbColumn = dbColumn,
            apiField = apiField,
            codeVariable = apiField,
            status = status,
            mappingSource = AssistTermMapping.MappingSource.Semantic,
            recommendationReason = recommendationReason,
        )

    private fun com.aulms.model.ValidationIssue.toWarning(): StandardViolationWarning =
        StandardViolationWarning(
            inputExpression = inputExpression,
            severity = severity,
            standardTerm = standardTerm,
            recommendedExpression = recommendedExpression,
            reason = reason,
        )
}
