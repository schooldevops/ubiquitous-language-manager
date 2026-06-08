package com.aulms.candidate.sql

import com.aulms.candidate.CandidateCommand
import com.aulms.candidate.CandidateNotFoundException
import com.aulms.candidate.CandidateRepository
import com.aulms.model.CandidateHistory
import com.aulms.model.CandidateStatus
import com.aulms.model.PageMetadata
import com.aulms.model.SimilarTerm
import com.aulms.model.TermCandidate
import com.aulms.model.TermCandidateListResponse
import com.aulms.model.TermCandidateSummary
import com.aulms.persistence.PostgresSequences
import com.querydsl.core.Tuple
import com.querydsl.sql.SQLQueryFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Repository
@Profile("postgres")
@Transactional
class QuerydslCandidateRepository(
    private val queryFactory: SQLQueryFactory,
    private val sequences: PostgresSequences,
) : CandidateRepository {

    private val c = QTermCandidate.termCandidate
    private val s = QCandidateSimilarTerm.candidateSimilarTerm
    private val h = QCandidateHistory.candidateHistory

    override fun create(command: CandidateCommand, similarTerms: List<SimilarTerm>): TermCandidate {
        val candidateId = "CAND-%06d".format(sequences.next("candidate_seq"))
        val now = OffsetDateTime.now()
        queryFactory.insert(c)
            .set(c.candidateId, candidateId)
            .set(c.koreanName, command.koreanName)
            .set(c.englishName, command.englishName)
            .set(c.englishAbbreviation, command.englishAbbreviation)
            .set(c.domainName, command.domainName)
            .set(c.businessDefinition, command.businessDefinition)
            .set(c.physicalType, command.physicalType)
            .set(c.digits, command.digits)
            .set(c.decimalPoint, command.decimalPoint)
            .set(c.status, CandidateStatus.Draft.value)
            .set(c.requestedBy, command.requestedBy)
            .set(c.usageContext, command.usageContext)
            .set(c.createdAt, now)
            .set(c.updatedAt, now)
            .execute()
        similarTerms.forEach { similar ->
            queryFactory.insert(s)
                .set(s.candidateId, candidateId)
                .set(s.termId, similar.termId)
                .set(s.koreanName, similar.koreanName)
                .set(s.englishName, similar.englishName)
                .set(s.dbColumn, similar.dbColumn)
                .set(s.apiField, similar.apiField)
                .set(s.reason, similar.reason)
                .execute()
        }
        saveHistory(candidateId, CandidateStatus.Draft, "신규 후보 등록", command.requestedBy, now)
        return get(candidateId)
    }

    @Transactional(readOnly = true)
    override fun list(q: String?, status: CandidateStatus?, domainName: String?, page: Int, size: Int): TermCandidateListResponse {
        val normalized = q?.trim()?.lowercase()
        val filtered = queryFactory.select(*c.all()).from(c).orderBy(c.candidateId.asc()).fetch().map { it.toRow() }
            .filter { normalized.isNullOrBlank() || it.koreanName.lowercase().contains(normalized) || it.englishName.lowercase().contains(normalized) || it.englishAbbreviation.lowercase().contains(normalized) }
            .filter { status == null || it.status == status }
            .filter { domainName.isNullOrBlank() || it.domainName == domainName }
        val fromIndex = (page * size).coerceAtMost(filtered.size)
        val toIndex = (fromIndex + size).coerceAtMost(filtered.size)
        val totalPages = if (filtered.isEmpty()) 0 else ((filtered.size - 1) / size) + 1
        return TermCandidateListResponse(
            items = filtered.subList(fromIndex, toIndex).map { it.toSummary() },
            page = PageMetadata(page = page, propertySize = size, totalElements = filtered.size.toLong(), totalPages = totalPages),
        )
    }

    @Transactional(readOnly = true)
    override fun get(candidateId: String): TermCandidate = rowOrThrow(candidateId).toDto()

    override fun updateStatus(candidateId: String, status: CandidateStatus, actor: String, reason: String, reviewedBy: String?, promotedTermId: String?): TermCandidate {
        val existing = rowOrThrow(candidateId)
        val now = OffsetDateTime.now()
        queryFactory.update(c).where(c.candidateId.eq(candidateId))
            .set(c.status, status.value)
            .set(c.reviewedBy, reviewedBy ?: existing.reviewedBy)
            .set(c.promotedTermId, promotedTermId ?: existing.promotedTermId)
            .set(c.updatedAt, now)
            .execute()
        saveHistory(candidateId, status, reason, actor, now)
        return get(candidateId)
    }

    private data class CandidateRow(
        val candidateId: String,
        val koreanName: String,
        val englishName: String,
        val englishAbbreviation: String,
        val domainName: String,
        val businessDefinition: String,
        val physicalType: String,
        val digits: Int,
        val decimalPoint: Int,
        val status: CandidateStatus,
        val requestedBy: String,
        val usageContext: String?,
        val reviewedBy: String?,
        val promotedTermId: String?,
        val createdAt: OffsetDateTime?,
        val updatedAt: OffsetDateTime?,
    )

    private fun rowOrThrow(candidateId: String): CandidateRow =
        queryFactory.select(*c.all()).from(c).where(c.candidateId.eq(candidateId)).fetchFirst()?.toRow()
            ?: throw CandidateNotFoundException(candidateId)

    private fun saveHistory(candidateId: String, status: CandidateStatus, reason: String, actor: String, now: OffsetDateTime) {
        queryFactory.insert(h)
            .set(h.historyId, "CAND-HIST-%06d".format(sequences.next("candidate_history_seq")))
            .set(h.candidateId, candidateId)
            .set(h.status, status.value)
            .set(h.reason, reason)
            .set(h.actor, actor)
            .set(h.createdAt, now)
            .execute()
    }

    private fun Tuple.toRow(): CandidateRow = CandidateRow(
        candidateId = get(c.candidateId)!!,
        koreanName = get(c.koreanName)!!,
        englishName = get(c.englishName)!!,
        englishAbbreviation = get(c.englishAbbreviation)!!,
        domainName = get(c.domainName)!!,
        businessDefinition = get(c.businessDefinition)!!,
        physicalType = get(c.physicalType)!!,
        digits = get(c.digits)!!,
        decimalPoint = get(c.decimalPoint)!!,
        status = CandidateStatus.forValue(get(c.status)!!),
        requestedBy = get(c.requestedBy)!!,
        usageContext = get(c.usageContext),
        reviewedBy = get(c.reviewedBy),
        promotedTermId = get(c.promotedTermId),
        createdAt = get(c.createdAt),
        updatedAt = get(c.updatedAt),
    )

    private fun CandidateRow.toDto(): TermCandidate = TermCandidate(
        candidateId = candidateId,
        koreanName = koreanName,
        englishName = englishName,
        englishAbbreviation = englishAbbreviation,
        domainName = domainName,
        businessDefinition = businessDefinition,
        physicalType = physicalType,
        digits = digits,
        decimalPoint = decimalPoint,
        status = status,
        requestedBy = requestedBy,
        similarTerms = queryFactory.select(*s.all()).from(s).where(s.candidateId.eq(candidateId)).orderBy(s.id.asc()).fetch().map {
            SimilarTerm(
                termId = it.get(s.termId)!!,
                koreanName = it.get(s.koreanName)!!,
                englishName = it.get(s.englishName)!!,
                dbColumn = it.get(s.dbColumn)!!,
                apiField = it.get(s.apiField)!!,
                reason = it.get(s.reason)!!,
            )
        },
        histories = queryFactory.select(*h.all()).from(h).where(h.candidateId.eq(candidateId)).orderBy(h.createdAt.asc()).fetch().map {
            CandidateHistory(
                historyId = it.get(h.historyId)!!,
                candidateId = it.get(h.candidateId)!!,
                status = CandidateStatus.forValue(it.get(h.status)!!),
                reason = it.get(h.reason)!!,
                actor = it.get(h.actor)!!,
                createdAt = it.get(h.createdAt)!!,
            )
        },
        usageContext = usageContext,
        reviewedBy = reviewedBy,
        promotedTermId = promotedTermId,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    private fun CandidateRow.toSummary(): TermCandidateSummary = TermCandidateSummary(
        candidateId = candidateId,
        koreanName = koreanName,
        englishName = englishName,
        englishAbbreviation = englishAbbreviation,
        domainName = domainName,
        status = status,
        requestedBy = requestedBy,
        promotedTermId = promotedTermId,
    )
}
