package com.aulms.ai

import com.aulms.graph.GraphSyncWorker
import com.aulms.model.GraphRecommendationContext
import com.aulms.model.RecommendationEvidence
import com.aulms.model.RecommendedTermDraft
import com.aulms.model.SearchResult
import com.aulms.model.SemanticSearchRequest
import com.aulms.model.SemanticSearchResult
import com.aulms.model.TermRecommendationRequest
import com.aulms.model.TermRecommendationResponse
import com.aulms.model.TermStatus
import com.aulms.term.TermRepository
import com.aulms.search.SearchNormalizer
import com.aulms.search.SearchService
import com.aulms.search.SemanticSearchService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TermRecommendationService(
    private val repository: TermRepository,
    private val searchService: SearchService,
    private val semanticSearchService: SemanticSearchService,
    private val graphSyncWorker: GraphSyncWorker,
    private val llmTermDraftGenerator: LlmTermDraftGenerator,
) {
    private val logger = LoggerFactory.getLogger(TermRecommendationService::class.java)

    fun recommend(request: TermRecommendationRequest): TermRecommendationResponse {
        val normalizedKoreanName = request.koreanName.trim().replace("\\s+".toRegex(), "")
        val exactMatches = searchService.exactSearch(request.koreanName, null).items.map { it.toEvidence(RecommendationEvidence.Source.Exact) }
        val aliasMatches = searchService.aliasSearch(request.koreanName).items.map { it.toEvidence(RecommendationEvidence.Source.Alias) }
        val semanticMatches = semanticSearchService.semanticSearch(
            SemanticSearchRequest(
                query = request.koreanName,
                statuses = listOf(TermStatus.Approved, TermStatus.Draft, TermStatus.Reviewing),
                domainNames = request.currentDomainName?.takeIf { it.isNotBlank() }?.let { listOf(it) },
                limit = 5,
            ),
        ).items.map { it.toEvidence() }

        val ragMatches = (exactMatches + aliasMatches + semanticMatches)
            .sortedWith(compareByDescending<RecommendationEvidence> { it.score }.thenBy { it.termId })
            .distinctBy { it.termId }
            .take(5)

        val inferredDomainName = inferDomainName(request, ragMatches, normalizedKoreanName)
        val graphContext = buildGraphContext(inferredDomainName, ragMatches.firstOrNull())
        val existingExact = exactMatches.firstOrNull {
            SearchNormalizer.normalize(it.standardTerm) == SearchNormalizer.normalize(normalizedKoreanName)
        }
        val recommendation: RecommendedTermDraft
        val usedLlm: Boolean
        if (existingExact != null) {
            recommendation = heuristicInferRecommendation(
                request = request,
                normalizedKoreanName = normalizedKoreanName,
                inferredDomainName = inferredDomainName,
                ragMatches = ragMatches,
                graphContext = graphContext,
                existingExact = existingExact,
            )
            usedLlm = false
        } else {
            val llmResult = try {
                llmTermDraftGenerator.generate(request, inferredDomainName, graphContext)
            } catch (ex: LlmUnavailableException) {
                logger.warn("LLM term draft unavailable, falling back to heuristic: {}", ex.message)
                null
            }
            if (llmResult != null) {
                recommendation = llmResult
                usedLlm = true
            } else {
                recommendation = heuristicInferRecommendation(
                    request = request,
                    normalizedKoreanName = normalizedKoreanName,
                    inferredDomainName = inferredDomainName,
                    ragMatches = ragMatches,
                    graphContext = graphContext,
                    existingExact = existingExact,
                )
                usedLlm = false
            }
        }

        return TermRecommendationResponse(
            inputKoreanName = request.koreanName,
            normalizedKoreanName = normalizedKoreanName,
            recommendation = recommendation,
            ragMatches = ragMatches,
            graphContext = graphContext,
            llmReasoning = llmReasoning(ragMatches, graphContext, recommendation, existingExact != null),
            warnings = warnings(normalizedKoreanName, ragMatches, existingExact != null, usedLlm),
        )
    }

    private fun inferDomainName(
        request: TermRecommendationRequest,
        ragMatches: List<RecommendationEvidence>,
        normalizedKoreanName: String,
    ): String {
        request.currentDomainName?.takeIf { it.isNotBlank() }?.let { return it }
        ragMatches.firstOrNull()?.domainName?.takeIf { it.isNotBlank() }?.let { return it }
        val domains = repository.searchDocuments().map { it.term.domainName }.distinct()
        return domains.firstOrNull { normalizedKoreanName.startsWith(it) } ?: "공통"
    }

    private fun buildGraphContext(
        inferredDomainName: String,
        anchor: RecommendationEvidence?,
    ): GraphRecommendationContext {
        val snapshot = graphSyncWorker.buildSnapshot()
        val nodeMap = snapshot.nodes.associateBy { it.nodeKey }
        val domainTerms = repository.searchDocuments()
            .filter { it.term.domainName == inferredDomainName && it.term.status == TermStatus.Approved }
            .map { it.term.koreanName }
            .distinct()
            .take(5)
        val anchorKey = anchor?.termId?.let { "term:$it" }
        val anchorEdges = snapshot.edges
            .filter { edge -> anchorKey != null && (edge.fromNodeKey == anchorKey || edge.toNodeKey == anchorKey) }
            .filter { it.relationshipType != "belongsTo" }

        val relatedFromEdges = anchorEdges.mapNotNull { edge ->
            val other = if (edge.fromNodeKey == anchorKey) edge.toNodeKey else edge.fromNodeKey
            nodeMap[other]?.properties?.get("koreanName")
        }

        val relationshipHints = anchorEdges.map { edge ->
            edge.properties["description"]
                ?: edge.properties["evidence"]
                ?: "${anchor?.standardTerm ?: inferredDomainName} 관련 관계가 graph snapshot 에 존재함"
        }.take(4)

        val relatedTerms = (relatedFromEdges + domainTerms).distinct().take(6)
        val hints = if (relationshipHints.isNotEmpty()) {
            relationshipHints
        } else {
            buildList {
                if (relatedTerms.isNotEmpty()) {
                    add("${inferredDomainName} 도메인 승인 용어 ${relatedTerms.joinToString(", ")} 와 함께 검토해야 함")
                }
                add("${inferredDomainName} 도메인 신규 속성은 대표 식별 용어를 기준으로 저장되는 경우가 많음")
            }
        }

        return GraphRecommendationContext(
            inferredDomainName = inferredDomainName,
            relatedTerms = relatedTerms,
            relationshipHints = hints.distinct(),
        )
    }

    private fun heuristicInferRecommendation(
        request: TermRecommendationRequest,
        normalizedKoreanName: String,
        inferredDomainName: String,
        ragMatches: List<RecommendationEvidence>,
        graphContext: GraphRecommendationContext,
        existingExact: RecommendationEvidence?,
    ): RecommendedTermDraft {
        if (existingExact != null) {
            val document = repository.searchDocuments().firstOrNull { it.term.termId == existingExact.termId }
            if (document != null) {
                return RecommendedTermDraft(
                    domainName = document.term.domainName,
                    usageType = document.term.usageType,
                    englishName = document.term.englishName,
                    englishAbbreviation = document.term.englishAbbreviation,
                    businessDefinition = request.currentBusinessDefinition?.takeIf { it.isNotBlank() } ?: document.term.businessDefinition,
                    usageContext = request.currentUsageContext?.takeIf { it.isNotBlank() } ?: (document.term.usageContext ?: ""),
                    physicalType = document.term.physicalType,
                    digits = document.term.digits,
                    decimalPoint = document.term.decimalPoint,
                    owner = document.term.owner,
                )
            }
        }

        val inferredPattern = inferPattern(normalizedKoreanName, ragMatches)
        val englishName = buildEnglishName(normalizedKoreanName, inferredDomainName, ragMatches)
        val englishAbbreviation = buildAbbreviation(normalizedKoreanName, inferredDomainName, inferredPattern)
        val businessDefinition = request.currentBusinessDefinition?.takeIf { it.isNotBlank() }
            ?: "${inferredDomainName} 도메인에서 $normalizedKoreanName 를 업무적으로 관리하기 위해 사용하는 표준 항목"
        val usageContext = request.currentUsageContext?.takeIf { it.isNotBlank() }
            ?: buildUsageContext(inferredDomainName, normalizedKoreanName, graphContext)

        return RecommendedTermDraft(
            domainName = inferredDomainName,
            usageType = "표준항목",
            englishName = englishName,
            englishAbbreviation = englishAbbreviation,
            businessDefinition = businessDefinition,
            usageContext = usageContext,
            physicalType = inferredPattern.physicalType,
            digits = inferredPattern.digits,
            decimalPoint = inferredPattern.decimalPoint,
            owner = "${inferredDomainName}도메인 데이터스튜어드",
        )
    }

    private fun buildEnglishName(
        koreanName: String,
        inferredDomainName: String,
        ragMatches: List<RecommendationEvidence>,
    ): String {
        val exactDocument = repository.searchDocuments().firstOrNull {
            SearchNormalizer.normalize(it.term.koreanName) == SearchNormalizer.normalize(koreanName)
        }
        if (exactDocument != null) {
            return exactDocument.term.englishName
        }

        val base = if (koreanName.startsWith(inferredDomainName)) koreanName.removePrefix(inferredDomainName) else koreanName
        val translatedBase = translatePhrase(base)
        val domainEnglish = domainEnglishMap[inferredDomainName] ?: ragMatches.firstOrNull()?.englishName?.substringBefore(" ") ?: "Common"
        return listOf(domainEnglish, translatedBase)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .replace("\\s+".toRegex(), " ")
            .trim()
    }

    private fun buildAbbreviation(
        koreanName: String,
        inferredDomainName: String,
        inferredPattern: InferredPattern,
    ): String {
        val exactDocument = repository.searchDocuments().firstOrNull {
            SearchNormalizer.normalize(it.term.koreanName) == SearchNormalizer.normalize(koreanName)
        }
        if (exactDocument != null) {
            return exactDocument.term.englishAbbreviation
        }

        val base = if (koreanName.startsWith(inferredDomainName)) koreanName.removePrefix(inferredDomainName) else koreanName
        val tokens = tokenizeForAbbreviation(base)
        val prefix = domainAbbreviationMap[inferredDomainName] ?: "CMN"
        val suffix = inferredPattern.suffixToken?.takeIf { it.isNotBlank() }
        val mergedTokens = if (suffix != null && tokens.lastOrNull() == suffix) tokens else tokens + listOfNotNull(suffix)
        return (listOf(prefix) + mergedTokens)
            .filter { it.isNotBlank() }
            .joinToString("_")
            .replace("__", "_")
    }

    private fun buildUsageContext(
        inferredDomainName: String,
        koreanName: String,
        graphContext: GraphRecommendationContext,
    ): String {
        val related = graphContext.relatedTerms.take(3)
        return if (related.isNotEmpty()) {
            "${inferredDomainName} 관련 화면, API, DB, 테스트 시나리오에서 $koreanName 표현으로 사용하며 ${related.joinToString(", ")} 와 함께 검토함"
        } else {
            "${inferredDomainName} 관련 화면, API, DB, 테스트 시나리오에서 $koreanName 표현으로 사용"
        }
    }

    private fun llmReasoning(
        ragMatches: List<RecommendationEvidence>,
        graphContext: GraphRecommendationContext,
        recommendation: RecommendedTermDraft,
        exactExisting: Boolean,
    ): String {
        if (exactExisting) {
            return "RAG 정확 검색에서 동일 표준 용어를 찾았기 때문에 기존 승인 표현을 그대로 재사용함"
        }
        val sourceTerms = ragMatches.take(2).joinToString(", ") { "${it.standardTerm}(${it.source.value})" }.ifBlank { "직접 일치 용어 없음" }
        val related = graphContext.relatedTerms.take(3).joinToString(", ").ifBlank { "관계 용어 부족" }
        return "RAG 근거 $sourceTerms, graph 문맥 $related 를 먼저 수집한 뒤 ${recommendation.englishName} 및 ${recommendation.englishAbbreviation} 후보를 추론함"
    }

    private fun warnings(
        normalizedKoreanName: String,
        ragMatches: List<RecommendationEvidence>,
        exactExisting: Boolean,
        usedLlm: Boolean,
    ): List<String> {
        val items = mutableListOf<String>()
        if (!usedLlm && !exactExisting) {
            items += "LLM 미사용(키 미설정 또는 호출 실패) 규칙기반 추천값이므로 검토가 필요함"
        }
        if (exactExisting) {
            items += "데이터 사전에 동일한 표준 용어가 이미 존재하므로 신규 등록보다 기존 용어 재사용이 우선임"
        } else {
            items += "데이터 사전에 동일한 Approved 용어는 없어 신규 후보 또는 신규 등록 검토가 필요함"
        }
        if (ragMatches.any { it.source == RecommendationEvidence.Source.Alias }) {
            items += "유사어 또는 비표준 표현과 혼동될 수 있으므로 승인 전 별칭/금지어 등록 여부를 함께 검토해야 함"
        }
        if (normalizedKoreanName.contains("시간대")) {
            items += "시간대가 코드 체계인지 자유 입력값인지에 따라 코드 용어와 속성 용어를 분리할지 검토해야 함"
        }
        return items.distinct()
    }

    private fun SearchResult.toEvidence(source: RecommendationEvidence.Source): RecommendationEvidence =
        RecommendationEvidence(
            termId = termId,
            standardTerm = standardTerm,
            englishName = englishName,
            dbColumn = dbColumn,
            apiField = apiField,
            domainName = domainName ?: "공통",
            source = source,
            score = score ?: if (source == RecommendationEvidence.Source.Exact) 1.0 else 0.9,
            reason = recommendations.firstOrNull()?.reason ?: businessDefinition ?: "검색 결과 기반 근거",
        )

    private fun SemanticSearchResult.toEvidence(): RecommendationEvidence =
        RecommendationEvidence(
            termId = termId,
            standardTerm = standardTerm,
            englishName = englishName,
            dbColumn = dbColumn,
            apiField = apiField,
            domainName = repository.searchDocuments().firstOrNull { it.term.termId == termId }?.term?.domainName ?: "공통",
            source = RecommendationEvidence.Source.Semantic,
            score = similarityScore,
            reason = recommendationReason,
        )

    private fun inferPattern(koreanName: String, ragMatches: List<RecommendationEvidence>): InferredPattern {
        val domain = ragMatches.firstOrNull()?.domainName
        val sameSuffixDocument = repository.searchDocuments()
            .filter { domain == null || it.term.domainName == domain }
            .firstOrNull { doc ->
                suffixCatalog.keys.any { koreanName.endsWith(it) && doc.term.koreanName.endsWith(it) }
            }
        if (sameSuffixDocument != null) {
            return InferredPattern(
                physicalType = sameSuffixDocument.term.physicalType,
                digits = sameSuffixDocument.term.digits,
                decimalPoint = sameSuffixDocument.term.decimalPoint,
                suffixToken = suffixCatalog.entries.firstOrNull { koreanName.endsWith(it.key) }?.value,
            )
        }

        suffixCatalog.entries.firstOrNull { koreanName.endsWith(it.key) }?.let { matched ->
            return when (matched.key) {
                "상태코드" -> InferredPattern("VARCHAR", 10, 0, "STS_CD")
                "코드" -> InferredPattern("VARCHAR", 10, 0, "CD")
                "번호" -> InferredPattern("VARCHAR", 20, 0, "NO")
                "일시" -> InferredPattern("TIMESTAMP", 14, 0, "DTTM")
                "일자" -> InferredPattern("DATE", 8, 0, "DT")
                "금액" -> InferredPattern("NUMERIC", 15, 2, "AMT")
                "명" -> InferredPattern("VARCHAR", 100, 0, "NM")
                "여부" -> InferredPattern("CHAR", 1, 0, "YN")
                "시간대" -> InferredPattern("VARCHAR", 40, 0, "TM_SLOT")
                else -> InferredPattern("VARCHAR", 100, 0, matched.value)
            }
        }

        return InferredPattern("VARCHAR", 100, 0, "VAL")
    }

    private fun translatePhrase(value: String): String {
        val words = mutableListOf<String>()
        var index = 0
        while (index < value.length) {
            val matched = translationCatalog.entries
                .sortedByDescending { it.key.length }
                .firstOrNull { value.startsWith(it.key, index) }
            if (matched != null) {
                words += matched.value
                index += matched.key.length
            } else {
                index += 1
            }
        }
        return words.joinToString(" ").replace("\\s+".toRegex(), " ").trim().ifBlank { "Term" }
    }

    private fun tokenizeForAbbreviation(value: String): List<String> {
        val tokens = mutableListOf<String>()
        var index = 0
        while (index < value.length) {
            val matched = abbreviationCatalog.entries
                .sortedByDescending { it.key.length }
                .firstOrNull { value.startsWith(it.key, index) }
            if (matched != null) {
                tokens += matched.value
                index += matched.key.length
            } else {
                index += 1
            }
        }
        return tokens.distinct()
    }

    private data class InferredPattern(
        val physicalType: String,
        val digits: Int,
        val decimalPoint: Int,
        val suffixToken: String?,
    )

    companion object {
        private val domainEnglishMap = mapOf(
            "고객" to "Customer",
            "주문" to "Order",
            "결제" to "Payment",
            "상품" to "Product",
            "계약" to "Contract",
            "청구" to "Billing",
            "상담" to "Consultation",
            "공통" to "Common",
        )

        private val domainAbbreviationMap = mapOf(
            "고객" to "CUST",
            "주문" to "ORD",
            "결제" to "PAY",
            "상품" to "PRD",
            "계약" to "CTRT",
            "청구" to "BILL",
            "상담" to "CNSL",
            "공통" to "CMN",
        )

        private val translationCatalog = linkedMapOf(
            "상태코드" to "Status Code",
            "시간대" to "Time Slot",
            "선호" to "Preferred",
            "배송" to "Delivery",
            "고객" to "Customer",
            "주문" to "Order",
            "결제" to "Payment",
            "상품" to "Product",
            "계약" to "Contract",
            "청구" to "Billing",
            "상담" to "Consultation",
            "번호" to "Number",
            "일자" to "Date",
            "일시" to "Date Time",
            "금액" to "Amount",
            "상태" to "Status",
            "코드" to "Code",
            "목록" to "List",
            "명" to "Name",
            "여부" to "Yn",
            "대표" to "Primary",
            "이미지" to "Image",
            "시간" to "Time",
        )

        private val abbreviationCatalog = linkedMapOf(
            "상태코드" to "STS_CD",
            "시간대" to "TM_SLOT",
            "선호" to "PREF",
            "배송" to "DLV",
            "고객" to "CUST",
            "주문" to "ORD",
            "결제" to "PAY",
            "상품" to "PRD",
            "계약" to "CTRT",
            "청구" to "BILL",
            "상담" to "CNSL",
            "번호" to "NO",
            "일자" to "DT",
            "일시" to "DTTM",
            "금액" to "AMT",
            "상태" to "STS",
            "코드" to "CD",
            "목록" to "LIST",
            "명" to "NM",
            "여부" to "YN",
            "대표" to "REP",
            "이미지" to "IMG",
            "시간" to "TM",
        )

        private val suffixCatalog = linkedMapOf(
            "상태코드" to "STS_CD",
            "시간대" to "TM_SLOT",
            "번호" to "NO",
            "일시" to "DTTM",
            "일자" to "DT",
            "금액" to "AMT",
            "코드" to "CD",
            "명" to "NM",
            "여부" to "YN",
        )
    }
}
