package com.aulms.upload.sql

import com.aulms.model.TermAlias
import com.aulms.model.TermExpression
import com.aulms.term.sql.QTerm
import com.aulms.term.sql.QTermAlias
import com.aulms.term.sql.QTermExpression
import com.aulms.term.sql.QTermRelationship
import com.aulms.upload.ParsedRelationship
import com.aulms.upload.ParsedTerm
import com.querydsl.sql.SQLQueryFactory
import com.querydsl.sql.dml.SQLUpdateClause
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

/** 업로드용 term/표현/별칭/관계 upsert. postgres 전용. */
@Repository
@Profile("postgres")
@Transactional
class QuerydslTermUpsertRepository(
    private val queryFactory: SQLQueryFactory,
    private val jdbc: JdbcTemplate,
) {
    private val t = QTerm.term
    private val e = QTermExpression.termExpression
    private val a = QTermAlias.termAlias
    private val r = QTermRelationship.termRelationship

    /** term 본문 upsert. 반환: true=INSERT, false=UPDATE. 필수필드 누락 시 IllegalArgumentException. */
    fun upsertTerm(term: ParsedTerm): Boolean {
        val exists = queryFactory.select(t.termId).from(t).where(t.termId.eq(term.termId)).fetchFirst() != null
        val now = OffsetDateTime.now()
        if (exists) {
            val clause: SQLUpdateClause = queryFactory.update(t).where(t.termId.eq(term.termId))
            term.domainName?.let { clause.set(t.domainName, it) }
            term.usageType?.let { clause.set(t.usageType, it) }
            term.koreanName?.let { clause.set(t.koreanName, it) }
            term.englishName?.let { clause.set(t.englishName, it) }
            term.englishAbbreviation?.let { clause.set(t.englishAbbreviation, it) }
            term.businessDefinition?.let { clause.set(t.businessDefinition, it) }
            term.physicalType?.let { clause.set(t.physicalType, it) }
            term.digits?.let { clause.set(t.digits, it) }
            term.decimalPoint?.let { clause.set(t.decimalPoint, it) }
            term.status?.let { clause.set(t.status, it.value) }
            term.owner?.let { clause.set(t.owner, it) }
            term.version?.let { clause.set(t.version, it) }
            term.usageContext?.let { clause.set(t.usageContext, it) }
            term.termNumber?.let { clause.set(t.termNumber, it) }
            clause.set(t.updatedAt, now)
            clause.execute()
            return false
        }
        requireFields(term)
        queryFactory.insert(t)
            .set(t.termId, term.termId)
            .set(t.termNumber, term.termNumber ?: "TERM-${term.termId.removePrefix("T-")}")
            .set(t.domainName, term.domainName)
            .set(t.usageType, term.usageType)
            .set(t.koreanName, term.koreanName)
            .set(t.englishName, term.englishName)
            .set(t.englishAbbreviation, term.englishAbbreviation)
            .set(t.businessDefinition, term.businessDefinition)
            .set(t.physicalType, term.physicalType)
            .set(t.digits, term.digits)
            .set(t.decimalPoint, term.decimalPoint)
            .set(t.status, term.status!!.value)
            .set(t.owner, term.owner)
            .set(t.version, term.version ?: "1.0")
            .set(t.usageContext, term.usageContext)
            .set(t.createdAt, now)
            .set(t.updatedAt, now)
            .execute()
        return true
    }

    private fun requireFields(term: ParsedTerm) {
        val missing = buildList {
            if (term.domainName == null) add("domainName")
            if (term.usageType == null) add("usageType")
            if (term.koreanName == null) add("koreanName")
            if (term.englishName == null) add("englishName")
            if (term.englishAbbreviation == null) add("englishAbbreviation")
            if (term.businessDefinition == null) add("businessDefinition")
            if (term.physicalType == null) add("physicalType")
            if (term.digits == null) add("digits")
            if (term.decimalPoint == null) add("decimalPoint")
            if (term.status == null) add("status")
            if (term.owner == null) add("owner")
        }
        require(missing.isEmpty()) { "신규 용어 필수필드 누락: ${missing.joinToString(",")}" }
    }

    fun upsertExpression(ex: TermExpression) {
        val exists = queryFactory.select(e.expressionId).from(e).where(e.expressionId.eq(ex.expressionId)).fetchFirst() != null
        if (exists) {
            queryFactory.update(e).where(e.expressionId.eq(ex.expressionId))
                .set(e.termId, ex.termId)
                .set(e.expressionType, ex.expressionType.value)
                .set(e.expressionValue, ex.expressionValue)
                .set(e.isStandard, ex.isStandard)
                .set(e.language, ex.language)
                .set(e.style, ex.style)
                .execute()
        } else {
            queryFactory.insert(e)
                .set(e.expressionId, ex.expressionId)
                .set(e.termId, ex.termId)
                .set(e.expressionType, ex.expressionType.value)
                .set(e.expressionValue, ex.expressionValue)
                .set(e.isStandard, ex.isStandard)
                .set(e.language, ex.language)
                .set(e.style, ex.style)
                .execute()
        }
    }

    fun upsertAlias(al: TermAlias) {
        val exists = queryFactory.select(a.aliasId).from(a).where(a.aliasId.eq(al.aliasId)).fetchFirst() != null
        if (exists) {
            queryFactory.update(a).where(a.aliasId.eq(al.aliasId))
                .set(a.termId, al.termId)
                .set(a.aliasName, al.aliasName)
                .set(a.aliasType, al.aliasType.value)
                .set(a.recommendationAction, al.recommendationAction)
                .set(a.reason, al.reason)
                .execute()
        } else {
            queryFactory.insert(a)
                .set(a.aliasId, al.aliasId)
                .set(a.termId, al.termId)
                .set(a.aliasName, al.aliasName)
                .set(a.aliasType, al.aliasType.value)
                .set(a.recommendationAction, al.recommendationAction)
                .set(a.reason, al.reason)
                .execute()
        }
    }

    /** 관계 upsert. source/target 둘 다 존재해야 함(아니면 예외). relationshipId 기준. */
    fun upsertRelationship(sourceTermId: String, rel: ParsedRelationship) {
        require(termExists(sourceTermId)) { "source 용어 없음: $sourceTermId" }
        require(termExists(rel.targetTermId)) { "target 용어 없음: ${rel.targetTermId}" }
        val exists = queryFactory.select(r.id).from(r).where(r.id.eq(rel.relationshipId)).fetchFirst() != null
        if (exists) {
            queryFactory.update(r).where(r.id.eq(rel.relationshipId))
                .set(r.sourceTermId, sourceTermId)
                .set(r.relationshipType, rel.relationshipType.value)
                .set(r.targetTermId, rel.targetTermId)
                .set(r.description, rel.description)
                .execute()
        } else {
            queryFactory.insert(r)
                .set(r.id, rel.relationshipId)
                .set(r.sourceTermId, sourceTermId)
                .set(r.relationshipType, rel.relationshipType.value)
                .set(r.targetTermId, rel.targetTermId)
                .set(r.description, rel.description)
                .execute()
        }
    }

    private fun termExists(termId: String): Boolean =
        queryFactory.select(t.termId).from(t).where(t.termId.eq(termId)).fetchFirst() != null

    /** 명시 ID 적재 후 시퀀스 정합(다음 신규 생성분 충돌 방지). */
    fun realignSequences() {
        jdbc.queryForObject("SELECT setval('term_expression_seq', (SELECT COALESCE(MAX(expression_id), 36) FROM term_expression))", Long::class.java)
        jdbc.queryForObject("SELECT setval('term_alias_seq', (SELECT COALESCE(MAX(CAST(SUBSTRING(alias_id FROM 3) AS INTEGER)), 13) FROM term_alias))", Long::class.java)
        jdbc.queryForObject("SELECT setval('term_relationship_id_seq', (SELECT COALESCE(MAX(id), 1) FROM term_relationship))", Long::class.java)
    }
}
