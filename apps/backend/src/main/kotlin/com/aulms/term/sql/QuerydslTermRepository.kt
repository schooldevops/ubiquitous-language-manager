package com.aulms.term.sql

import com.aulms.model.AliasType
import com.aulms.model.ExpressionType
import com.aulms.model.PageMetadata
import com.aulms.model.Term
import com.aulms.model.TermAlias
import com.aulms.model.TermChangeHistory
import com.aulms.model.TermExpression
import com.aulms.model.TermListResponse
import com.aulms.model.TermStatus
import com.aulms.model.TermSummary
import com.aulms.persistence.PostgresSequences
import com.aulms.term.TermCommand
import com.aulms.term.TermConflictException
import com.aulms.term.TermNotFoundException
import com.aulms.term.TermRelationshipRecord
import com.aulms.term.TermRepository
import com.aulms.term.TermSearchDocument
import com.querydsl.core.Tuple
import com.querydsl.sql.SQLQueryFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Repository
@Profile("postgres")
@Transactional
class QuerydslTermRepository(
    private val queryFactory: SQLQueryFactory,
    private val sequences: PostgresSequences,
) : TermRepository {

    private val t = QTerm.term
    private val e = QTermExpression.termExpression
    private val a = QTermAlias.termAlias
    private val r = QTermRelationship.termRelationship
    private val h = QTermChangeHistory.termChangeHistory

    @Transactional(readOnly = true)
    override fun list(q: String?, domainName: String?, status: TermStatus?, page: Int, size: Int): TermListResponse {
        val expressionsByTerm = allExpressions()
        val aliasesByTerm = allAliases()
        val filtered = allTerms()
            .filter { domainName == null || it.domainName == domainName }
            .filter { status == null || it.status == status }
            .filter { q.isNullOrBlank() || matches(it, q, expressionsByTerm[it.termId].orEmpty(), aliasesByTerm[it.termId].orEmpty()) }
            .sortedBy { it.termId }
        val fromIndex = (page * size).coerceAtMost(filtered.size)
        val toIndex = (fromIndex + size).coerceAtMost(filtered.size)
        val items = filtered.subList(fromIndex, toIndex).map { it.toSummary(expressionsByTerm[it.termId].orEmpty()) }
        val totalPages = if (filtered.isEmpty()) 0 else ((filtered.size - 1) / size) + 1
        return TermListResponse(
            items = items,
            page = PageMetadata(page = page, propertySize = size, totalElements = filtered.size.toLong(), totalPages = totalPages),
        )
    }

    @Transactional(readOnly = true)
    override fun get(termId: String): Term = termOrThrow(termId).toFullTerm()

    override fun create(command: TermCommand): Term {
        val exists = queryFactory.select(t.termId).from(t)
            .where(t.koreanName.eq(command.koreanName).or(t.englishAbbreviation.eq(command.englishAbbreviation)))
            .fetchFirst() != null
        if (exists) throw TermConflictException("Term already exists: ${command.koreanName}/${command.englishAbbreviation}")
        val next = sequences.next("term_seq")
        val termId = "T-%06d".format(next)
        val now = OffsetDateTime.now()
        queryFactory.insert(t)
            .set(t.termId, termId)
            .set(t.termNumber, "TERM-%06d".format(next))
            .set(t.domainName, command.domainName)
            .set(t.usageType, command.usageType)
            .set(t.koreanName, command.koreanName)
            .set(t.englishName, command.englishName)
            .set(t.englishAbbreviation, command.englishAbbreviation)
            .set(t.businessDefinition, command.businessDefinition)
            .set(t.physicalType, command.physicalType)
            .set(t.digits, command.digits)
            .set(t.decimalPoint, command.decimalPoint)
            .set(t.status, command.status.value)
            .set(t.owner, command.owner)
            .set(t.version, "1.0")
            .set(t.usageContext, command.usageContext)
            .set(t.createdAt, now)
            .set(t.updatedAt, now)
            .execute()
        addHistory(termId, "CREATE", null, command.status, "표준 용어 등록", null, null)
        return get(termId)
    }

    override fun update(termId: String, command: TermCommand, version: String, reason: String): Term {
        val previous = termOrThrow(termId)
        queryFactory.update(t).where(t.termId.eq(termId))
            .set(t.domainName, command.domainName)
            .set(t.usageType, command.usageType)
            .set(t.koreanName, command.koreanName)
            .set(t.englishName, command.englishName)
            .set(t.englishAbbreviation, command.englishAbbreviation)
            .set(t.businessDefinition, command.businessDefinition)
            .set(t.physicalType, command.physicalType)
            .set(t.digits, command.digits)
            .set(t.decimalPoint, command.decimalPoint)
            .set(t.status, command.status.value)
            .set(t.owner, command.owner)
            .set(t.version, version)
            .set(t.usageContext, command.usageContext)
            .set(t.updatedAt, OffsetDateTime.now())
            .execute()
        addHistory(termId, "UPDATE", previous.status, command.status, reason, null, null)
        return get(termId)
    }

    override fun approve(termId: String, approver: String, reason: String): Term {
        val previous = termOrThrow(termId)
        queryFactory.update(t).where(t.termId.eq(termId))
            .set(t.status, TermStatus.Approved.value)
            .set(t.updatedAt, OffsetDateTime.now())
            .execute()
        addHistory(termId, "APPROVE", previous.status, TermStatus.Approved, reason, null, approver)
        return get(termId)
    }

    override fun deprecate(termId: String, approver: String, replacementTermId: String, reason: String, impactAnalysisId: String?): Term {
        termOrThrow(replacementTermId)
        val previous = termOrThrow(termId)
        queryFactory.update(t).where(t.termId.eq(termId))
            .set(t.status, TermStatus.Deprecated.value)
            .set(t.updatedAt, OffsetDateTime.now())
            .execute()
        addHistory(termId, "DEPRECATE", previous.status, TermStatus.Deprecated, reason, null, approver, impactAnalysisId)
        return get(termId)
    }

    @Transactional(readOnly = true)
    override fun listExpressions(termId: String): List<TermExpression> {
        termOrThrow(termId)
        return expressionsOf(termId)
    }

    override fun addExpression(termId: String, expressionType: ExpressionType, expressionValue: String, language: String?, style: String?, standard: Boolean): TermExpression {
        val term = termOrThrow(termId)
        val expressionId = sequences.next("term_expression_seq")
        queryFactory.insert(e)
            .set(e.expressionId, expressionId)
            .set(e.termId, termId)
            .set(e.expressionType, expressionType.value)
            .set(e.expressionValue, expressionValue)
            .set(e.isStandard, standard)
            .set(e.language, language)
            .set(e.style, style)
            .execute()
        addHistory(termId, "ADD_EXPRESSION", term.status, term.status, "표현 매핑 등록: $expressionValue", null, null)
        return TermExpression(expressionId, termId, expressionType, expressionValue, standard, language, style)
    }

    @Transactional(readOnly = true)
    override fun listAliases(termId: String): List<TermAlias> {
        termOrThrow(termId)
        return aliasesOf(termId)
    }

    override fun addAlias(termId: String, aliasName: String, aliasType: AliasType, recommendationAction: String, reason: String): TermAlias {
        val term = termOrThrow(termId)
        val aliasId = "A-%06d".format(sequences.next("term_alias_seq"))
        queryFactory.insert(a)
            .set(a.aliasId, aliasId)
            .set(a.termId, termId)
            .set(a.aliasName, aliasName)
            .set(a.aliasType, aliasType.value)
            .set(a.recommendationAction, recommendationAction)
            .set(a.reason, reason)
            .execute()
        addHistory(termId, "ADD_ALIAS", term.status, term.status, "별칭 등록: $aliasName", null, null)
        return TermAlias(aliasId, termId, aliasName, aliasType, recommendationAction, reason)
    }

    @Transactional(readOnly = true)
    override fun searchDocuments(): List<TermSearchDocument> {
        val expressionsByTerm = allExpressions()
        val aliasesByTerm = allAliases()
        return allTerms().map { row ->
            TermSearchDocument(
                term = row.toTerm(expressionsByTerm[row.termId].orEmpty(), aliasesByTerm[row.termId].orEmpty()),
                expressions = expressionsByTerm[row.termId].orEmpty(),
                aliases = aliasesByTerm[row.termId].orEmpty(),
            )
        }
    }

    @Transactional(readOnly = true)
    override fun relationshipDocuments(): List<TermRelationshipRecord> =
        queryFactory.select(r.sourceTermId, r.relationshipType, r.targetTermId, r.description).from(r).fetch().map { tuple ->
            TermRelationshipRecord(
                sourceTermId = tuple.get(r.sourceTermId)!!,
                relationshipType = com.aulms.model.RelationshipType.forValue(tuple.get(r.relationshipType)!!),
                targetTermId = tuple.get(r.targetTermId)!!,
                description = tuple.get(r.description).orEmpty(),
            )
        }

    @Transactional(readOnly = true)
    override fun listHistory(termId: String, page: Int, size: Int): Pair<List<TermChangeHistory>, PageMetadata> {
        termOrThrow(termId)
        val filtered = queryFactory.select(*h.all()).from(h)
            .where(h.termId.eq(termId)).orderBy(h.changeHistoryId.asc()).fetch()
            .map { it.toHistory() }
        val fromIndex = (page * size).coerceAtMost(filtered.size)
        val toIndex = (fromIndex + size).coerceAtMost(filtered.size)
        val totalPages = if (filtered.isEmpty()) 0 else ((filtered.size - 1) / size) + 1
        return filtered.subList(fromIndex, toIndex) to PageMetadata(page, size, filtered.size.toLong(), totalPages)
    }

    // --- internal row model + mappers -------------------------------------

    private data class TermRow(
        val termId: String,
        val termNumber: String,
        val domainName: String,
        val usageType: String,
        val koreanName: String,
        val englishName: String,
        val englishAbbreviation: String,
        val businessDefinition: String,
        val physicalType: String,
        val digits: Int,
        val decimalPoint: Int,
        val status: TermStatus,
        val owner: String,
        val version: String,
        val usageContext: String?,
        val createdAt: OffsetDateTime?,
        val updatedAt: OffsetDateTime?,
    )

    private fun allTerms(): List<TermRow> =
        queryFactory.select(*t.all()).from(t).orderBy(t.termId.asc()).fetch().map { it.toRow() }

    private fun termOrThrow(termId: String): TermRow =
        queryFactory.select(*t.all()).from(t).where(t.termId.eq(termId)).fetchFirst()?.toRow()
            ?: throw TermNotFoundException(termId)

    private fun expressionsOf(termId: String): List<TermExpression> =
        queryFactory.select(*e.all()).from(e).where(e.termId.eq(termId)).orderBy(e.expressionId.asc()).fetch().map { it.toExpression() }

    private fun aliasesOf(termId: String): List<TermAlias> =
        queryFactory.select(*a.all()).from(a).where(a.termId.eq(termId)).orderBy(a.aliasId.asc()).fetch().map { it.toAlias() }

    private fun allExpressions(): Map<String, List<TermExpression>> =
        queryFactory.select(*e.all()).from(e).orderBy(e.expressionId.asc()).fetch().map { it.toExpression() }.groupBy { it.termId }

    private fun allAliases(): Map<String, List<TermAlias>> =
        queryFactory.select(*a.all()).from(a).orderBy(a.aliasId.asc()).fetch().map { it.toAlias() }.groupBy { it.termId }

    private fun matches(term: TermRow, query: String, expressions: List<TermExpression>, aliases: List<TermAlias>): Boolean {
        val normalized = query.trim()
        return term.koreanName.contains(normalized, ignoreCase = true) ||
            term.englishName.contains(normalized, ignoreCase = true) ||
            term.englishAbbreviation.equals(normalized, ignoreCase = true) ||
            expressions.any { it.expressionValue.equals(normalized, ignoreCase = true) } ||
            aliases.any { it.aliasName.equals(normalized, ignoreCase = true) }
    }

    private fun addHistory(
        termId: String,
        changeType: String,
        previousStatus: TermStatus?,
        newStatus: TermStatus?,
        reason: String,
        requestedBy: String?,
        approvedBy: String?,
        impactAnalysisId: String? = null,
    ) {
        queryFactory.insert(h)
            .set(h.changeHistoryId, sequences.next("term_change_history_seq"))
            .set(h.termId, termId)
            .set(h.changeType, changeType)
            .set(h.reason, reason)
            .set(h.previousStatus, previousStatus?.value)
            .set(h.newStatus, newStatus?.value)
            .set(h.requestedBy, requestedBy)
            .set(h.approvedBy, approvedBy)
            .set(h.impactAnalysisId, impactAnalysisId)
            .set(h.createdAt, OffsetDateTime.now())
            .execute()
    }

    private fun Tuple.toRow(): TermRow = TermRow(
        termId = get(t.termId)!!,
        termNumber = get(t.termNumber)!!,
        domainName = get(t.domainName)!!,
        usageType = get(t.usageType)!!,
        koreanName = get(t.koreanName)!!,
        englishName = get(t.englishName)!!,
        englishAbbreviation = get(t.englishAbbreviation)!!,
        businessDefinition = get(t.businessDefinition)!!,
        physicalType = get(t.physicalType)!!,
        digits = get(t.digits)!!,
        decimalPoint = get(t.decimalPoint)!!,
        status = TermStatus.forValue(get(t.status)!!),
        owner = get(t.owner)!!,
        version = get(t.version)!!,
        usageContext = get(t.usageContext),
        createdAt = get(t.createdAt),
        updatedAt = get(t.updatedAt),
    )

    private fun Tuple.toExpression(): TermExpression = TermExpression(
        expressionId = get(e.expressionId)!!,
        termId = get(e.termId)!!,
        expressionType = ExpressionType.forValue(get(e.expressionType)!!),
        expressionValue = get(e.expressionValue)!!,
        isStandard = get(e.isStandard)!!,
        language = get(e.language),
        style = get(e.style),
    )

    private fun Tuple.toAlias(): TermAlias = TermAlias(
        aliasId = get(a.aliasId)!!,
        termId = get(a.termId)!!,
        aliasName = get(a.aliasName)!!,
        aliasType = AliasType.forValue(get(a.aliasType)!!),
        recommendationAction = get(a.recommendationAction)!!,
        reason = get(a.reason)!!,
    )

    private fun Tuple.toHistory(): TermChangeHistory = TermChangeHistory(
        changeHistoryId = get(h.changeHistoryId)!!,
        changeType = get(h.changeType)!!,
        reason = get(h.reason)!!,
        createdAt = get(h.createdAt)!!,
        termId = get(h.termId),
        previousStatus = get(h.previousStatus)?.let { TermStatus.forValue(it) },
        newStatus = get(h.newStatus)?.let { TermStatus.forValue(it) },
        requestedBy = get(h.requestedBy),
        approvedBy = get(h.approvedBy),
        impactAnalysisId = get(h.impactAnalysisId),
    )

    private fun TermRow.toTerm(expressions: List<TermExpression>, aliases: List<TermAlias>): Term = Term(
        termId = termId,
        termNumber = termNumber,
        domainName = domainName,
        usageType = usageType,
        koreanName = koreanName,
        englishName = englishName,
        englishAbbreviation = englishAbbreviation,
        businessDefinition = businessDefinition,
        physicalType = physicalType,
        digits = digits,
        decimalPoint = decimalPoint,
        status = status,
        owner = owner,
        version = version,
        expressions = expressions,
        aliases = aliases,
        usageContext = usageContext,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    private fun TermRow.toFullTerm(): Term = toTerm(expressionsOf(termId), aliasesOf(termId))

    private fun TermRow.toSummary(expressions: List<TermExpression>): TermSummary = TermSummary(
        termId = termId,
        termNumber = termNumber,
        domainName = domainName,
        koreanName = koreanName,
        englishName = englishName,
        englishAbbreviation = englishAbbreviation,
        status = status,
        apiFieldName = expressions.firstOrNull { it.expressionType == ExpressionType.API_FIELD }?.expressionValue,
        relatedSystems = listOf(domainName),
    )
}
