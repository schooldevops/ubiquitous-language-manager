package com.aulms.prompt.sql

import com.aulms.model.PromptTemplate
import com.aulms.model.PromptTemplateHistory
import com.aulms.model.PromptTemplateListResponse
import com.aulms.model.PromptTemplateStatus
import com.aulms.model.PromptTemplateSummary
import com.aulms.model.PromptTemplateType
import com.aulms.model.PromptTemplateVariable
import com.aulms.model.PromptTemplateVersion
import com.aulms.model.PromptTemplateVersionListResponse
import com.aulms.prompt.PromptTemplateNotFoundException
import com.aulms.prompt.PromptTemplateRepository
import com.querydsl.core.Tuple
import com.querydsl.sql.SQLQueryFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Profile("postgres")
@Transactional(readOnly = true)
class QuerydslPromptTemplateRepository(
    private val queryFactory: SQLQueryFactory,
) : PromptTemplateRepository {

    private val p = QPromptTemplate.promptTemplate
    private val v = QPromptTemplateVariable.promptTemplateVariable
    private val h = QPromptTemplateHistory.promptTemplateHistory
    private val ver = QPromptTemplateVersion.promptTemplateVersion

    override fun list(type: PromptTemplateType?, status: PromptTemplateStatus?): PromptTemplateListResponse {
        val items = queryFactory.select(*p.all()).from(p).orderBy(p.templateId.asc()).fetch().map { it.toRow() }
            .filter { type == null || it.type == type }
            .filter { status == null || it.status == status }
            .map { it.toSummary() }
        return PromptTemplateListResponse(items = items)
    }

    override fun get(templateId: String): PromptTemplate = rowOrThrow(templateId).toDto()

    override fun listVersions(templateId: String): PromptTemplateVersionListResponse {
        rowOrThrow(templateId)
        val items = queryFactory.select(*ver.all()).from(ver).where(ver.templateId.eq(templateId)).orderBy(ver.id.asc()).fetch().map {
            PromptTemplateVersion(
                templateId = it.get(ver.templateId)!!,
                version = it.get(ver.version)!!,
                status = PromptTemplateStatus.forValue(it.get(ver.status)!!),
                changeReason = it.get(ver.changeReason)!!,
                createdBy = it.get(ver.createdBy)!!,
                createdAt = it.get(ver.createdAt)!!,
            )
        }
        return PromptTemplateVersionListResponse(items = items)
    }

    private data class TemplateRow(
        val templateId: String,
        val type: PromptTemplateType,
        val name: String,
        val version: String,
        val status: PromptTemplateStatus,
        val description: String,
        val body: String,
        val versionPolicy: String,
        val createdAt: java.time.OffsetDateTime,
        val updatedAt: java.time.OffsetDateTime,
    )

    private fun rowOrThrow(templateId: String): TemplateRow =
        queryFactory.select(*p.all()).from(p).where(p.templateId.eq(templateId)).fetchFirst()?.toRow()
            ?: throw PromptTemplateNotFoundException(templateId)

    private fun Tuple.toRow(): TemplateRow = TemplateRow(
        templateId = get(p.templateId)!!,
        type = PromptTemplateType.forValue(get(p.type)!!),
        name = get(p.name)!!,
        version = get(p.version)!!,
        status = PromptTemplateStatus.forValue(get(p.status)!!),
        description = get(p.description)!!,
        body = get(p.body)!!,
        versionPolicy = get(p.versionPolicy)!!,
        createdAt = get(p.createdAt)!!,
        updatedAt = get(p.updatedAt)!!,
    )

    private fun TemplateRow.toSummary(): PromptTemplateSummary = PromptTemplateSummary(
        templateId = templateId,
        type = type,
        name = name,
        version = version,
        status = status,
        description = description,
        updatedAt = updatedAt,
    )

    private fun TemplateRow.toDto(): PromptTemplate = PromptTemplate(
        templateId = templateId,
        type = type,
        name = name,
        version = version,
        status = status,
        description = description,
        body = body,
        variables = queryFactory.select(*v.all()).from(v).where(v.templateId.eq(templateId)).orderBy(v.ordinal.asc()).fetch().map {
            PromptTemplateVariable(
                name = it.get(v.name)!!,
                description = it.get(v.description)!!,
                required = it.get(v.required)!!,
                source = PromptTemplateVariable.Source.forValue(it.get(v.source)!!),
            )
        },
        versionPolicy = versionPolicy,
        histories = queryFactory.select(*h.all()).from(h).where(h.templateId.eq(templateId)).orderBy(h.createdAt.asc()).fetch().map {
            PromptTemplateHistory(
                historyId = it.get(h.historyId)!!,
                templateId = it.get(h.templateId)!!,
                version = it.get(h.version)!!,
                changeType = PromptTemplateHistory.ChangeType.forValue(it.get(h.changeType)!!),
                reason = it.get(h.reason)!!,
                actor = it.get(h.actor)!!,
                createdAt = it.get(h.createdAt)!!,
            )
        },
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
