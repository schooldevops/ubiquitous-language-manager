package com.aulms.candidate.sql

import com.querydsl.core.types.PathMetadataFactory.forVariable
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.sql.ColumnMetadata
import com.querydsl.sql.RelationalPathBase
import java.sql.Types
import java.time.OffsetDateTime

/** Querydsl-SQL Q-type: term_candidate */
class QTermCandidate(variable: String) : RelationalPathBase<QTermCandidate>(QTermCandidate::class.java, forVariable(variable), "public", "term_candidate") {
    val candidateId: StringPath = createString("candidateId")
    val koreanName: StringPath = createString("koreanName")
    val englishName: StringPath = createString("englishName")
    val englishAbbreviation: StringPath = createString("englishAbbreviation")
    val domainName: StringPath = createString("domainName")
    val businessDefinition: StringPath = createString("businessDefinition")
    val physicalType: StringPath = createString("physicalType")
    val digits: NumberPath<Int> = createNumber("digits", Int::class.javaObjectType)
    val decimalPoint: NumberPath<Int> = createNumber("decimalPoint", Int::class.javaObjectType)
    val status: StringPath = createString("status")
    val requestedBy: StringPath = createString("requestedBy")
    val usageContext: StringPath = createString("usageContext")
    val reviewedBy: StringPath = createString("reviewedBy")
    val promotedTermId: StringPath = createString("promotedTermId")
    val createdAt: DateTimePath<OffsetDateTime> = createDateTime("createdAt", OffsetDateTime::class.java)
    val updatedAt: DateTimePath<OffsetDateTime> = createDateTime("updatedAt", OffsetDateTime::class.java)

    init {
        addMetadata(candidateId, ColumnMetadata.named("candidate_id").withIndex(1).ofType(Types.VARCHAR))
        addMetadata(koreanName, ColumnMetadata.named("korean_name").withIndex(2).ofType(Types.VARCHAR))
        addMetadata(englishName, ColumnMetadata.named("english_name").withIndex(3).ofType(Types.VARCHAR))
        addMetadata(englishAbbreviation, ColumnMetadata.named("english_abbreviation").withIndex(4).ofType(Types.VARCHAR))
        addMetadata(domainName, ColumnMetadata.named("domain_name").withIndex(5).ofType(Types.VARCHAR))
        addMetadata(businessDefinition, ColumnMetadata.named("business_definition").withIndex(6).ofType(Types.VARCHAR))
        addMetadata(physicalType, ColumnMetadata.named("physical_type").withIndex(7).ofType(Types.VARCHAR))
        addMetadata(digits, ColumnMetadata.named("digits").withIndex(8).ofType(Types.INTEGER))
        addMetadata(decimalPoint, ColumnMetadata.named("decimal_point").withIndex(9).ofType(Types.INTEGER))
        addMetadata(status, ColumnMetadata.named("status").withIndex(10).ofType(Types.VARCHAR))
        addMetadata(requestedBy, ColumnMetadata.named("requested_by").withIndex(11).ofType(Types.VARCHAR))
        addMetadata(usageContext, ColumnMetadata.named("usage_context").withIndex(12).ofType(Types.VARCHAR))
        addMetadata(reviewedBy, ColumnMetadata.named("reviewed_by").withIndex(13).ofType(Types.VARCHAR))
        addMetadata(promotedTermId, ColumnMetadata.named("promoted_term_id").withIndex(14).ofType(Types.VARCHAR))
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(15).ofType(Types.TIMESTAMP_WITH_TIMEZONE))
        addMetadata(updatedAt, ColumnMetadata.named("updated_at").withIndex(16).ofType(Types.TIMESTAMP_WITH_TIMEZONE))
    }

    companion object {
        @JvmField val termCandidate = QTermCandidate("term_candidate")
    }
}

/** Querydsl-SQL Q-type: candidate_similar_term */
class QCandidateSimilarTerm(variable: String) : RelationalPathBase<QCandidateSimilarTerm>(QCandidateSimilarTerm::class.java, forVariable(variable), "public", "candidate_similar_term") {
    val id: NumberPath<Long> = createNumber("id", Long::class.javaObjectType)
    val candidateId: StringPath = createString("candidateId")
    val termId: StringPath = createString("termId")
    val koreanName: StringPath = createString("koreanName")
    val englishName: StringPath = createString("englishName")
    val dbColumn: StringPath = createString("dbColumn")
    val apiField: StringPath = createString("apiField")
    val reason: StringPath = createString("reason")

    init {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT))
        addMetadata(candidateId, ColumnMetadata.named("candidate_id").withIndex(2).ofType(Types.VARCHAR))
        addMetadata(termId, ColumnMetadata.named("term_id").withIndex(3).ofType(Types.VARCHAR))
        addMetadata(koreanName, ColumnMetadata.named("korean_name").withIndex(4).ofType(Types.VARCHAR))
        addMetadata(englishName, ColumnMetadata.named("english_name").withIndex(5).ofType(Types.VARCHAR))
        addMetadata(dbColumn, ColumnMetadata.named("db_column").withIndex(6).ofType(Types.VARCHAR))
        addMetadata(apiField, ColumnMetadata.named("api_field").withIndex(7).ofType(Types.VARCHAR))
        addMetadata(reason, ColumnMetadata.named("reason").withIndex(8).ofType(Types.VARCHAR))
    }

    companion object {
        @JvmField val candidateSimilarTerm = QCandidateSimilarTerm("candidate_similar_term")
    }
}

/** Querydsl-SQL Q-type: candidate_history */
class QCandidateHistory(variable: String) : RelationalPathBase<QCandidateHistory>(QCandidateHistory::class.java, forVariable(variable), "public", "candidate_history") {
    val historyId: StringPath = createString("historyId")
    val candidateId: StringPath = createString("candidateId")
    val status: StringPath = createString("status")
    val reason: StringPath = createString("reason")
    val actor: StringPath = createString("actor")
    val createdAt: DateTimePath<OffsetDateTime> = createDateTime("createdAt", OffsetDateTime::class.java)

    init {
        addMetadata(historyId, ColumnMetadata.named("history_id").withIndex(1).ofType(Types.VARCHAR))
        addMetadata(candidateId, ColumnMetadata.named("candidate_id").withIndex(2).ofType(Types.VARCHAR))
        addMetadata(status, ColumnMetadata.named("status").withIndex(3).ofType(Types.VARCHAR))
        addMetadata(reason, ColumnMetadata.named("reason").withIndex(4).ofType(Types.VARCHAR))
        addMetadata(actor, ColumnMetadata.named("actor").withIndex(5).ofType(Types.VARCHAR))
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(6).ofType(Types.TIMESTAMP_WITH_TIMEZONE))
    }

    companion object {
        @JvmField val candidateHistory = QCandidateHistory("candidate_history")
    }
}
