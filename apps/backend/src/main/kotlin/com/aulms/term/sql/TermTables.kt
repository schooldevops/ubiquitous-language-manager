package com.aulms.term.sql

import com.querydsl.core.types.PathMetadataFactory.forVariable
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.sql.ColumnMetadata
import com.querydsl.sql.RelationalPathBase
import java.sql.Types
import java.time.OffsetDateTime

/** Querydsl-SQL Q-type: term */
class QTerm(variable: String) : RelationalPathBase<QTerm>(QTerm::class.java, forVariable(variable), "public", "term") {
    val termId: StringPath = createString("termId")
    val termNumber: StringPath = createString("termNumber")
    val domainName: StringPath = createString("domainName")
    val usageType: StringPath = createString("usageType")
    val koreanName: StringPath = createString("koreanName")
    val englishName: StringPath = createString("englishName")
    val englishAbbreviation: StringPath = createString("englishAbbreviation")
    val businessDefinition: StringPath = createString("businessDefinition")
    val physicalType: StringPath = createString("physicalType")
    val digits: NumberPath<Int> = createNumber("digits", Int::class.javaObjectType)
    val decimalPoint: NumberPath<Int> = createNumber("decimalPoint", Int::class.javaObjectType)
    val status: StringPath = createString("status")
    val owner: StringPath = createString("owner")
    val version: StringPath = createString("version")
    val usageContext: StringPath = createString("usageContext")
    val createdAt: DateTimePath<OffsetDateTime> = createDateTime("createdAt", OffsetDateTime::class.java)
    val updatedAt: DateTimePath<OffsetDateTime> = createDateTime("updatedAt", OffsetDateTime::class.java)

    init {
        addMetadata(termId, ColumnMetadata.named("term_id").withIndex(1).ofType(Types.VARCHAR))
        addMetadata(termNumber, ColumnMetadata.named("term_number").withIndex(2).ofType(Types.VARCHAR))
        addMetadata(domainName, ColumnMetadata.named("domain_name").withIndex(3).ofType(Types.VARCHAR))
        addMetadata(usageType, ColumnMetadata.named("usage_type").withIndex(4).ofType(Types.VARCHAR))
        addMetadata(koreanName, ColumnMetadata.named("korean_name").withIndex(5).ofType(Types.VARCHAR))
        addMetadata(englishName, ColumnMetadata.named("english_name").withIndex(6).ofType(Types.VARCHAR))
        addMetadata(englishAbbreviation, ColumnMetadata.named("english_abbreviation").withIndex(7).ofType(Types.VARCHAR))
        addMetadata(businessDefinition, ColumnMetadata.named("business_definition").withIndex(8).ofType(Types.VARCHAR))
        addMetadata(physicalType, ColumnMetadata.named("physical_type").withIndex(9).ofType(Types.VARCHAR))
        addMetadata(digits, ColumnMetadata.named("digits").withIndex(10).ofType(Types.INTEGER))
        addMetadata(decimalPoint, ColumnMetadata.named("decimal_point").withIndex(11).ofType(Types.INTEGER))
        addMetadata(status, ColumnMetadata.named("status").withIndex(12).ofType(Types.VARCHAR))
        addMetadata(owner, ColumnMetadata.named("owner").withIndex(13).ofType(Types.VARCHAR))
        addMetadata(version, ColumnMetadata.named("version").withIndex(14).ofType(Types.VARCHAR))
        addMetadata(usageContext, ColumnMetadata.named("usage_context").withIndex(15).ofType(Types.VARCHAR))
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(16).ofType(Types.TIMESTAMP_WITH_TIMEZONE))
        addMetadata(updatedAt, ColumnMetadata.named("updated_at").withIndex(17).ofType(Types.TIMESTAMP_WITH_TIMEZONE))
    }

    companion object {
        @JvmField val term = QTerm("term")
    }
}

/** Querydsl-SQL Q-type: term_expression */
class QTermExpression(variable: String) : RelationalPathBase<QTermExpression>(QTermExpression::class.java, forVariable(variable), "public", "term_expression") {
    val expressionId: NumberPath<Long> = createNumber("expressionId", Long::class.javaObjectType)
    val termId: StringPath = createString("termId")
    val expressionType: StringPath = createString("expressionType")
    val expressionValue: StringPath = createString("expressionValue")
    val isStandard = createBoolean("isStandard")
    val language: StringPath = createString("language")
    val style: StringPath = createString("style")

    init {
        addMetadata(expressionId, ColumnMetadata.named("expression_id").withIndex(1).ofType(Types.BIGINT))
        addMetadata(termId, ColumnMetadata.named("term_id").withIndex(2).ofType(Types.VARCHAR))
        addMetadata(expressionType, ColumnMetadata.named("expression_type").withIndex(3).ofType(Types.VARCHAR))
        addMetadata(expressionValue, ColumnMetadata.named("expression_value").withIndex(4).ofType(Types.VARCHAR))
        addMetadata(isStandard, ColumnMetadata.named("is_standard").withIndex(5).ofType(Types.BOOLEAN))
        addMetadata(language, ColumnMetadata.named("language").withIndex(6).ofType(Types.VARCHAR))
        addMetadata(style, ColumnMetadata.named("style").withIndex(7).ofType(Types.VARCHAR))
    }

    companion object {
        @JvmField val termExpression = QTermExpression("term_expression")
    }
}

/** Querydsl-SQL Q-type: term_alias */
class QTermAlias(variable: String) : RelationalPathBase<QTermAlias>(QTermAlias::class.java, forVariable(variable), "public", "term_alias") {
    val aliasId: StringPath = createString("aliasId")
    val termId: StringPath = createString("termId")
    val aliasName: StringPath = createString("aliasName")
    val aliasType: StringPath = createString("aliasType")
    val recommendationAction: StringPath = createString("recommendationAction")
    val reason: StringPath = createString("reason")

    init {
        addMetadata(aliasId, ColumnMetadata.named("alias_id").withIndex(1).ofType(Types.VARCHAR))
        addMetadata(termId, ColumnMetadata.named("term_id").withIndex(2).ofType(Types.VARCHAR))
        addMetadata(aliasName, ColumnMetadata.named("alias_name").withIndex(3).ofType(Types.VARCHAR))
        addMetadata(aliasType, ColumnMetadata.named("alias_type").withIndex(4).ofType(Types.VARCHAR))
        addMetadata(recommendationAction, ColumnMetadata.named("recommendation_action").withIndex(5).ofType(Types.VARCHAR))
        addMetadata(reason, ColumnMetadata.named("reason").withIndex(6).ofType(Types.VARCHAR))
    }

    companion object {
        @JvmField val termAlias = QTermAlias("term_alias")
    }
}

/** Querydsl-SQL Q-type: term_relationship */
class QTermRelationship(variable: String) : RelationalPathBase<QTermRelationship>(QTermRelationship::class.java, forVariable(variable), "public", "term_relationship") {
    val id: NumberPath<Long> = createNumber("id", Long::class.javaObjectType)
    val sourceTermId: StringPath = createString("sourceTermId")
    val relationshipType: StringPath = createString("relationshipType")
    val targetTermId: StringPath = createString("targetTermId")
    val description: StringPath = createString("description")

    init {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT))
        addMetadata(sourceTermId, ColumnMetadata.named("source_term_id").withIndex(2).ofType(Types.VARCHAR))
        addMetadata(relationshipType, ColumnMetadata.named("relationship_type").withIndex(3).ofType(Types.VARCHAR))
        addMetadata(targetTermId, ColumnMetadata.named("target_term_id").withIndex(4).ofType(Types.VARCHAR))
        addMetadata(description, ColumnMetadata.named("description").withIndex(5).ofType(Types.VARCHAR))
    }

    companion object {
        @JvmField val termRelationship = QTermRelationship("term_relationship")
    }
}

/** Querydsl-SQL Q-type: term_change_history */
class QTermChangeHistory(variable: String) : RelationalPathBase<QTermChangeHistory>(QTermChangeHistory::class.java, forVariable(variable), "public", "term_change_history") {
    val changeHistoryId: NumberPath<Long> = createNumber("changeHistoryId", Long::class.javaObjectType)
    val termId: StringPath = createString("termId")
    val changeType: StringPath = createString("changeType")
    val reason: StringPath = createString("reason")
    val previousStatus: StringPath = createString("previousStatus")
    val newStatus: StringPath = createString("newStatus")
    val requestedBy: StringPath = createString("requestedBy")
    val approvedBy: StringPath = createString("approvedBy")
    val impactAnalysisId: StringPath = createString("impactAnalysisId")
    val createdAt: DateTimePath<OffsetDateTime> = createDateTime("createdAt", OffsetDateTime::class.java)

    init {
        addMetadata(changeHistoryId, ColumnMetadata.named("change_history_id").withIndex(1).ofType(Types.BIGINT))
        addMetadata(termId, ColumnMetadata.named("term_id").withIndex(2).ofType(Types.VARCHAR))
        addMetadata(changeType, ColumnMetadata.named("change_type").withIndex(3).ofType(Types.VARCHAR))
        addMetadata(reason, ColumnMetadata.named("reason").withIndex(4).ofType(Types.VARCHAR))
        addMetadata(previousStatus, ColumnMetadata.named("previous_status").withIndex(5).ofType(Types.VARCHAR))
        addMetadata(newStatus, ColumnMetadata.named("new_status").withIndex(6).ofType(Types.VARCHAR))
        addMetadata(requestedBy, ColumnMetadata.named("requested_by").withIndex(7).ofType(Types.VARCHAR))
        addMetadata(approvedBy, ColumnMetadata.named("approved_by").withIndex(8).ofType(Types.VARCHAR))
        addMetadata(impactAnalysisId, ColumnMetadata.named("impact_analysis_id").withIndex(9).ofType(Types.VARCHAR))
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(10).ofType(Types.TIMESTAMP_WITH_TIMEZONE))
    }

    companion object {
        @JvmField val termChangeHistory = QTermChangeHistory("term_change_history")
    }
}
