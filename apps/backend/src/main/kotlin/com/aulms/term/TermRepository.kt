package com.aulms.term

import com.aulms.model.AliasType
import com.aulms.model.ExpressionType
import com.aulms.model.PageMetadata
import com.aulms.model.RelationshipType
import com.aulms.model.Term
import com.aulms.model.TermAlias
import com.aulms.model.TermChangeHistory
import com.aulms.model.TermExpression
import com.aulms.model.TermListResponse
import com.aulms.model.TermStatus

/**
 * 표준 용어 저장소. 영속화 모드별 구현:
 *  - [InMemoryTermRepository] : 기본(memory) 프로파일, seed 데이터
 *  - JpaTermRepository        : postgres 프로파일
 */
interface TermRepository {
    fun list(q: String?, domainName: String?, status: TermStatus?, page: Int, size: Int): TermListResponse
    fun get(termId: String): Term
    fun create(command: TermCommand): Term
    fun update(termId: String, command: TermCommand, version: String, reason: String): Term
    fun approve(termId: String, approver: String, reason: String): Term
    fun deprecate(termId: String, approver: String, replacementTermId: String, reason: String, impactAnalysisId: String?): Term
    fun listExpressions(termId: String): List<TermExpression>
    fun addExpression(termId: String, expressionType: ExpressionType, expressionValue: String, language: String?, style: String?, standard: Boolean): TermExpression
    fun deleteExpressions(termId: String)
    fun listAliases(termId: String): List<TermAlias>
    fun addAlias(termId: String, aliasName: String, aliasType: AliasType, recommendationAction: String, reason: String): TermAlias
    fun deleteAliases(termId: String)
    fun searchDocuments(): List<TermSearchDocument>
    fun relationshipDocuments(): List<TermRelationshipRecord>
    fun listHistory(termId: String, page: Int, size: Int): Pair<List<TermChangeHistory>, PageMetadata>
}

data class TermCommand(
    val domainName: String,
    val usageType: String,
    val koreanName: String,
    val englishName: String,
    val englishAbbreviation: String,
    val businessDefinition: String,
    val usageContext: String?,
    val physicalType: String,
    val digits: Int,
    val decimalPoint: Int,
    val owner: String,
    val status: TermStatus,
)

data class TermSearchDocument(
    val term: Term,
    val expressions: List<TermExpression>,
    val aliases: List<TermAlias>,
) {
    fun apiField(): String = expressions.firstOrNull { it.expressionType == ExpressionType.API_FIELD }?.expressionValue
        ?: term.englishName.split(" ").map { it.lowercase() }.mapIndexed { index, part ->
            if (index == 0) part else part.replaceFirstChar { char -> char.uppercase() }
        }.joinToString("")

    fun codeVariable(): String = expressions.firstOrNull { it.expressionType == ExpressionType.CODE_VARIABLE }?.expressionValue ?: apiField()
}

data class TermRelationshipRecord(
    val sourceTermId: String,
    val relationshipType: RelationshipType,
    val targetTermId: String,
    val description: String,
)
