package com.aulms.persistence

import com.aulms.candidate.sql.QCandidateHistory
import com.aulms.candidate.sql.QCandidateSimilarTerm
import com.aulms.candidate.sql.QTermCandidate
import com.aulms.prompt.InMemoryPromptTemplateRepository
import com.aulms.prompt.sql.QPromptTemplate
import com.aulms.prompt.sql.QPromptTemplateHistory
import com.aulms.prompt.sql.QPromptTemplateVariable
import com.aulms.prompt.sql.QPromptTemplateVersion
import com.aulms.term.InMemoryTermRepository
import com.aulms.term.sql.QTerm
import com.aulms.term.sql.QTermAlias
import com.aulms.term.sql.QTermChangeHistory
import com.aulms.term.sql.QTermExpression
import com.aulms.term.sql.QTermRelationship
import com.querydsl.sql.SQLQueryFactory
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * postgres 모드 초기 seed. memory 모드와 동일 데모 데이터를 동일 소스(InMemory* 저장소)에서
 * 복사해 Querydsl-SQL 로 적재한다 → seed 단일 정의(drift 방지). 빈 테이블일 때만 동작(멱등).
 */
@Component
@Profile("postgres")
class PostgresDataSeeder(
    private val queryFactory: SQLQueryFactory,
    private val jdbc: JdbcTemplate,
) : ApplicationRunner {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun run(args: ApplicationArguments) {
        seedTerms()
        seedPromptTemplates()
    }

    private fun seedTerms() {
        val t = QTerm.term
        if (queryFactory.from(t).fetchCount() > 0L) return
        val e = QTermExpression.termExpression
        val a = QTermAlias.termAlias
        val r = QTermRelationship.termRelationship

        val seed = InMemoryTermRepository()
        val documents = seed.searchDocuments()
        documents.forEach { doc ->
            val term = doc.term
            queryFactory.insert(t)
                .set(t.termId, term.termId)
                .set(t.termNumber, term.termNumber)
                .set(t.domainName, term.domainName)
                .set(t.usageType, term.usageType)
                .set(t.koreanName, term.koreanName)
                .set(t.englishName, term.englishName)
                .set(t.englishAbbreviation, term.englishAbbreviation)
                .set(t.businessDefinition, term.businessDefinition)
                .set(t.physicalType, term.physicalType)
                .set(t.digits, term.digits)
                .set(t.decimalPoint, term.decimalPoint)
                .set(t.status, term.status.value)
                .set(t.owner, term.owner)
                .set(t.version, term.version)
                .set(t.usageContext, term.usageContext)
                .set(t.createdAt, term.createdAt)
                .set(t.updatedAt, term.updatedAt)
                .execute()
        }
        documents.flatMap { it.expressions }.forEach { ex ->
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
        documents.flatMap { it.aliases }.forEach { al ->
            queryFactory.insert(a)
                .set(a.aliasId, al.aliasId)
                .set(a.termId, al.termId)
                .set(a.aliasName, al.aliasName)
                .set(a.aliasType, al.aliasType.value)
                .set(a.recommendationAction, al.recommendationAction)
                .set(a.reason, al.reason)
                .execute()
        }
        seed.relationshipDocuments().forEach { rel ->
            queryFactory.insert(r)
                .set(r.sourceTermId, rel.sourceTermId)
                .set(r.relationshipType, rel.relationshipType.value)
                .set(r.targetTermId, rel.targetTermId)
                .set(r.description, rel.description)
                .execute()
        }
        // seed 가 사용한 ID 이후로 시퀀스 전진(새 생성분 충돌 방지).
        jdbc.queryForObject("SELECT setval('term_expression_seq', (SELECT COALESCE(MAX(expression_id), 36) FROM term_expression))", Long::class.java)
        jdbc.queryForObject("SELECT setval('term_alias_seq', (SELECT COALESCE(MAX(CAST(SUBSTRING(alias_id FROM 3) AS INTEGER)), 13) FROM term_alias))", Long::class.java)
        log.info("Seeded {} terms into postgres (querydsl-sql)", documents.size)
    }

    private fun seedPromptTemplates() {
        val p = QPromptTemplate.promptTemplate
        if (queryFactory.from(p).fetchCount() > 0L) return
        val v = QPromptTemplateVariable.promptTemplateVariable
        val h = QPromptTemplateHistory.promptTemplateHistory
        val ver = QPromptTemplateVersion.promptTemplateVersion

        val seed = InMemoryPromptTemplateRepository()
        seed.list(null, null).items.forEach { summary ->
            val template = seed.get(summary.templateId)
            queryFactory.insert(p)
                .set(p.templateId, template.templateId)
                .set(p.type, template.type.value)
                .set(p.name, template.name)
                .set(p.version, template.version)
                .set(p.status, template.status.value)
                .set(p.description, template.description)
                .set(p.body, template.body)
                .set(p.versionPolicy, template.versionPolicy)
                .set(p.createdAt, template.createdAt)
                .set(p.updatedAt, template.updatedAt)
                .execute()
            template.variables.forEachIndexed { index, variable ->
                queryFactory.insert(v)
                    .set(v.templateId, template.templateId)
                    .set(v.name, variable.name)
                    .set(v.description, variable.description)
                    .set(v.required, variable.required)
                    .set(v.source, variable.source.value)
                    .set(v.ordinal, index)
                    .execute()
            }
            template.histories.forEach { history ->
                queryFactory.insert(h)
                    .set(h.historyId, history.historyId)
                    .set(h.templateId, history.templateId)
                    .set(h.version, history.version)
                    .set(h.changeType, history.changeType.value)
                    .set(h.reason, history.reason)
                    .set(h.actor, history.actor)
                    .set(h.createdAt, history.createdAt)
                    .execute()
            }
            seed.listVersions(template.templateId).items.forEach { version ->
                queryFactory.insert(ver)
                    .set(ver.templateId, version.templateId)
                    .set(ver.version, version.version)
                    .set(ver.status, version.status.value)
                    .set(ver.changeReason, version.changeReason)
                    .set(ver.createdBy, version.createdBy)
                    .set(ver.createdAt, version.createdAt)
                    .execute()
            }
        }
        log.info("Seeded prompt templates into postgres (querydsl-sql)")
    }
}
