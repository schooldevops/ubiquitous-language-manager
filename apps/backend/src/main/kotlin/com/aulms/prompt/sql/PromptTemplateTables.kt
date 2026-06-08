package com.aulms.prompt.sql

import com.querydsl.core.types.PathMetadataFactory.forVariable
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.sql.ColumnMetadata
import com.querydsl.sql.RelationalPathBase
import java.sql.Types
import java.time.OffsetDateTime

/** Querydsl-SQL Q-type: prompt_template */
class QPromptTemplate(variable: String) : RelationalPathBase<QPromptTemplate>(QPromptTemplate::class.java, forVariable(variable), "public", "prompt_template") {
    val templateId: StringPath = createString("templateId")
    val type: StringPath = createString("type")
    val name: StringPath = createString("name")
    val version: StringPath = createString("version")
    val status: StringPath = createString("status")
    val description: StringPath = createString("description")
    val body: StringPath = createString("body")
    val versionPolicy: StringPath = createString("versionPolicy")
    val createdAt: DateTimePath<OffsetDateTime> = createDateTime("createdAt", OffsetDateTime::class.java)
    val updatedAt: DateTimePath<OffsetDateTime> = createDateTime("updatedAt", OffsetDateTime::class.java)

    init {
        addMetadata(templateId, ColumnMetadata.named("template_id").withIndex(1).ofType(Types.VARCHAR))
        addMetadata(type, ColumnMetadata.named("type").withIndex(2).ofType(Types.VARCHAR))
        addMetadata(name, ColumnMetadata.named("name").withIndex(3).ofType(Types.VARCHAR))
        addMetadata(version, ColumnMetadata.named("version").withIndex(4).ofType(Types.VARCHAR))
        addMetadata(status, ColumnMetadata.named("status").withIndex(5).ofType(Types.VARCHAR))
        addMetadata(description, ColumnMetadata.named("description").withIndex(6).ofType(Types.VARCHAR))
        addMetadata(body, ColumnMetadata.named("body").withIndex(7).ofType(Types.VARCHAR))
        addMetadata(versionPolicy, ColumnMetadata.named("version_policy").withIndex(8).ofType(Types.VARCHAR))
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(9).ofType(Types.TIMESTAMP_WITH_TIMEZONE))
        addMetadata(updatedAt, ColumnMetadata.named("updated_at").withIndex(10).ofType(Types.TIMESTAMP_WITH_TIMEZONE))
    }

    companion object {
        @JvmField val promptTemplate = QPromptTemplate("prompt_template")
    }
}

/** Querydsl-SQL Q-type: prompt_template_variable */
class QPromptTemplateVariable(variable: String) : RelationalPathBase<QPromptTemplateVariable>(QPromptTemplateVariable::class.java, forVariable(variable), "public", "prompt_template_variable") {
    val id: NumberPath<Long> = createNumber("id", Long::class.javaObjectType)
    val templateId: StringPath = createString("templateId")
    val name: StringPath = createString("name")
    val description: StringPath = createString("description")
    val required = createBoolean("required")
    val source: StringPath = createString("source")
    val ordinal: NumberPath<Int> = createNumber("ordinal", Int::class.javaObjectType)

    init {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT))
        addMetadata(templateId, ColumnMetadata.named("template_id").withIndex(2).ofType(Types.VARCHAR))
        addMetadata(name, ColumnMetadata.named("name").withIndex(3).ofType(Types.VARCHAR))
        addMetadata(description, ColumnMetadata.named("description").withIndex(4).ofType(Types.VARCHAR))
        addMetadata(required, ColumnMetadata.named("required").withIndex(5).ofType(Types.BOOLEAN))
        addMetadata(source, ColumnMetadata.named("source").withIndex(6).ofType(Types.VARCHAR))
        addMetadata(ordinal, ColumnMetadata.named("ordinal").withIndex(7).ofType(Types.INTEGER))
    }

    companion object {
        @JvmField val promptTemplateVariable = QPromptTemplateVariable("prompt_template_variable")
    }
}

/** Querydsl-SQL Q-type: prompt_template_history */
class QPromptTemplateHistory(variable: String) : RelationalPathBase<QPromptTemplateHistory>(QPromptTemplateHistory::class.java, forVariable(variable), "public", "prompt_template_history") {
    val historyId: StringPath = createString("historyId")
    val templateId: StringPath = createString("templateId")
    val version: StringPath = createString("version")
    val changeType: StringPath = createString("changeType")
    val reason: StringPath = createString("reason")
    val actor: StringPath = createString("actor")
    val createdAt: DateTimePath<OffsetDateTime> = createDateTime("createdAt", OffsetDateTime::class.java)

    init {
        addMetadata(historyId, ColumnMetadata.named("history_id").withIndex(1).ofType(Types.VARCHAR))
        addMetadata(templateId, ColumnMetadata.named("template_id").withIndex(2).ofType(Types.VARCHAR))
        addMetadata(version, ColumnMetadata.named("version").withIndex(3).ofType(Types.VARCHAR))
        addMetadata(changeType, ColumnMetadata.named("change_type").withIndex(4).ofType(Types.VARCHAR))
        addMetadata(reason, ColumnMetadata.named("reason").withIndex(5).ofType(Types.VARCHAR))
        addMetadata(actor, ColumnMetadata.named("actor").withIndex(6).ofType(Types.VARCHAR))
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(7).ofType(Types.TIMESTAMP_WITH_TIMEZONE))
    }

    companion object {
        @JvmField val promptTemplateHistory = QPromptTemplateHistory("prompt_template_history")
    }
}

/** Querydsl-SQL Q-type: prompt_template_version */
class QPromptTemplateVersion(variable: String) : RelationalPathBase<QPromptTemplateVersion>(QPromptTemplateVersion::class.java, forVariable(variable), "public", "prompt_template_version") {
    val id: NumberPath<Long> = createNumber("id", Long::class.javaObjectType)
    val templateId: StringPath = createString("templateId")
    val version: StringPath = createString("version")
    val status: StringPath = createString("status")
    val changeReason: StringPath = createString("changeReason")
    val createdBy: StringPath = createString("createdBy")
    val createdAt: DateTimePath<OffsetDateTime> = createDateTime("createdAt", OffsetDateTime::class.java)

    init {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT))
        addMetadata(templateId, ColumnMetadata.named("template_id").withIndex(2).ofType(Types.VARCHAR))
        addMetadata(version, ColumnMetadata.named("version").withIndex(3).ofType(Types.VARCHAR))
        addMetadata(status, ColumnMetadata.named("status").withIndex(4).ofType(Types.VARCHAR))
        addMetadata(changeReason, ColumnMetadata.named("change_reason").withIndex(5).ofType(Types.VARCHAR))
        addMetadata(createdBy, ColumnMetadata.named("created_by").withIndex(6).ofType(Types.VARCHAR))
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(7).ofType(Types.TIMESTAMP_WITH_TIMEZONE))
    }

    companion object {
        @JvmField val promptTemplateVersion = QPromptTemplateVersion("prompt_template_version")
    }
}
