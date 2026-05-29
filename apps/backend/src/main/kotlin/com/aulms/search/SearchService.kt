package com.aulms.search

import com.aulms.model.AliasType
import com.aulms.model.DeprecatedSearchResponse
import com.aulms.model.DeprecatedSearchResult
import com.aulms.model.ExpressionType
import com.aulms.model.MatchedExpression
import com.aulms.model.PageMetadata
import com.aulms.model.Recommendation
import com.aulms.model.SearchResponse
import com.aulms.model.SearchResult
import com.aulms.model.TermAlias
import com.aulms.model.TermExpression
import com.aulms.model.TermStatus
import com.aulms.term.TermRepository
import com.aulms.term.TermSearchDocument
import org.springframework.stereotype.Service

@Service
class SearchService(private val repository: TermRepository) {
    fun exactSearch(query: String, status: TermStatus?): SearchResponse {
        val normalizedQuery = SearchNormalizer.normalize(query)
        val results = repository.searchDocuments()
            .filter { status == null || it.term.status == status }
            .mapNotNull { document ->
                val match = document.standardExpressions()
                    .firstOrNull { SearchNormalizer.normalize(it.expressionValue) == normalizedQuery }
                    ?: document.coreTermExpressions()
                        .firstOrNull { SearchNormalizer.normalize(it.expressionValue) == normalizedQuery }
                match?.let {
                    document.toSearchResult(
                        matchedExpression = MatchedExpression(
                            expressionType = it.expressionType,
                            expressionValue = it.expressionValue,
                            matchType = MatchedExpression.MatchType.Exact,
                            inputExpression = query,
                        ),
                        recommendation = document.standardRecommendation(
                            action = Recommendation.Action.UseStandardTerm,
                            reason = "입력 표현이 표준 용어 표현과 일치함",
                            severity = Recommendation.Severity.Info,
                        ),
                        score = 1.0,
                    )
                }
            }

        return SearchResponse(query = query, items = results)
    }

    fun aliasSearch(query: String): SearchResponse {
        val normalizedQuery = SearchNormalizer.normalize(query)
        val results = repository.searchDocuments()
            .mapNotNull { document ->
                val alias = document.aliases.firstOrNull { SearchNormalizer.normalize(it.aliasName) == normalizedQuery }
                alias?.let {
                    val recommendation = document.aliasRecommendation(it)
                    document.toSearchResult(
                        matchedExpression = MatchedExpression(
                            expressionType = aliasExpressionType(it.aliasName),
                            expressionValue = it.aliasName,
                            matchType = if (it.aliasType == AliasType.Deprecated) {
                                MatchedExpression.MatchType.Deprecated
                            } else {
                                MatchedExpression.MatchType.Alias
                            },
                            inputExpression = query,
                        ),
                        recommendation = recommendation,
                        score = if (it.aliasType == AliasType.Synonym) 0.96 else 0.9,
                    )
                }
            }

        return SearchResponse(query = query, items = results)
    }

    fun domainSearch(domainName: String, status: TermStatus?, page: Int, size: Int): SearchResponse {
        val filtered = repository.searchDocuments()
            .filter { it.term.domainName == domainName }
            .filter { status == null || it.term.status == status }
        val fromIndex = (page * size).coerceAtMost(filtered.size)
        val toIndex = (fromIndex + size).coerceAtMost(filtered.size)
        val items = filtered.subList(fromIndex, toIndex).map { document ->
            document.toSearchResult(
                matchedExpression = MatchedExpression(
                    expressionType = ExpressionType.Korean,
                    expressionValue = domainName,
                    matchType = MatchedExpression.MatchType.Domain,
                    inputExpression = domainName,
                ),
                recommendation = document.standardRecommendation(
                    action = Recommendation.Action.UseStandardTerm,
                    reason = "${domainName} 도메인의 표준 용어임",
                    severity = Recommendation.Severity.Info,
                ),
                score = 1.0,
            )
        }
        val totalPages = if (filtered.isEmpty()) 0 else ((filtered.size - 1) / size) + 1

        return SearchResponse(
            query = domainName,
            items = items,
            page = PageMetadata(page = page, propertySize = size, totalElements = filtered.size.toLong(), totalPages = totalPages),
        )
    }

    fun deprecatedSearch(query: String): DeprecatedSearchResponse {
        val normalizedQuery = SearchNormalizer.normalize(query)
        val items = repository.searchDocuments()
            .mapNotNull { document ->
                val alias = document.aliases.firstOrNull {
                    SearchNormalizer.normalize(it.aliasName) == normalizedQuery &&
                        (it.aliasType == AliasType.Forbidden || it.aliasType == AliasType.Deprecated)
                }
                alias?.let {
                    val recommendation = document.aliasRecommendation(it)
                    DeprecatedSearchResult(
                        deprecatedExpression = it.aliasName,
                        reason = it.reason,
                        replacementTerm = document.toSearchResult(
                            matchedExpression = MatchedExpression(
                                expressionType = aliasExpressionType(it.aliasName),
                                expressionValue = it.aliasName,
                                matchType = MatchedExpression.MatchType.Deprecated,
                                inputExpression = query,
                            ),
                            recommendation = recommendation,
                            score = 0.99,
                        ),
                        recommendation = recommendation,
                    )
                }
            }

        return DeprecatedSearchResponse(query = query, items = items)
    }

    private fun TermSearchDocument.standardExpressions(): List<TermExpression> =
        expressions.filter { it.isStandard }

    private fun TermSearchDocument.coreTermExpressions(): List<TermExpression> = listOf(
        TermExpression(0, term.termId, ExpressionType.Korean, term.koreanName, true),
        TermExpression(0, term.termId, ExpressionType.English, term.englishName, true),
        TermExpression(0, term.termId, ExpressionType.DB_COLUMN, term.englishAbbreviation, true),
    )

    private fun TermSearchDocument.toSearchResult(
        matchedExpression: MatchedExpression,
        recommendation: Recommendation,
        score: Double,
    ): SearchResult = SearchResult(
        termId = term.termId,
        standardTerm = term.koreanName,
        englishName = term.englishName,
        dbColumn = term.englishAbbreviation,
        apiField = apiField(),
        status = term.status,
        matchedExpressions = listOf(matchedExpression),
        recommendations = listOf(recommendation),
        codeVariable = codeVariable(),
        domainName = term.domainName,
        businessDefinition = term.businessDefinition,
        usageContext = term.usageContext,
        score = score,
    )

    private fun TermSearchDocument.standardRecommendation(
        action: Recommendation.Action,
        reason: String,
        severity: Recommendation.Severity,
    ): Recommendation = Recommendation(
        action = if (term.status == TermStatus.Deprecated) Recommendation.Action.ReplaceDeprecatedTerm else action,
        recommendedTermId = term.termId,
        recommendedTerm = term.koreanName,
        recommendedExpression = apiField(),
        reason = if (term.status == TermStatus.Deprecated) "폐기된 용어이므로 대체 표준 용어 확인이 필요함" else reason,
        severity = if (term.status == TermStatus.Deprecated) Recommendation.Severity.Error else severity,
    )

    private fun TermSearchDocument.aliasRecommendation(alias: TermAlias): Recommendation {
        val action = when (alias.aliasType) {
            AliasType.Synonym -> Recommendation.Action.UseStandardTerm
            AliasType.Forbidden, AliasType.Deprecated -> Recommendation.Action.ReplaceDeprecatedTerm
            AliasType.NeedsContext -> Recommendation.Action.ConfirmContext
        }
        val severity = when (alias.aliasType) {
            AliasType.Synonym -> Recommendation.Severity.Warning
            AliasType.Forbidden, AliasType.Deprecated -> Recommendation.Severity.Error
            AliasType.NeedsContext -> Recommendation.Severity.Warning
        }
        val expression = if (alias.aliasName.all { it.isUpperCase() || it == '_' || it.isDigit() }) term.englishAbbreviation else apiField()

        return Recommendation(
            action = action,
            recommendedTermId = term.termId,
            recommendedTerm = term.koreanName,
            recommendedExpression = expression,
            reason = "${alias.reason}. 권장 조치: ${alias.recommendationAction}",
            severity = severity,
        )
    }

    private fun aliasExpressionType(aliasName: String): ExpressionType = when {
        aliasName.all { it.isUpperCase() || it == '_' || it.isDigit() } -> ExpressionType.DB_COLUMN
        aliasName.firstOrNull()?.isLowerCase() == true -> ExpressionType.API_FIELD
        else -> ExpressionType.Korean
    }
}
