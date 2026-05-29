package com.aulms.candidate

import com.aulms.model.CandidateHistory
import com.aulms.model.CandidateStatus
import com.aulms.model.PageMetadata
import com.aulms.model.SimilarTerm
import com.aulms.model.TermCandidate
import com.aulms.model.TermCandidateListResponse
import com.aulms.model.TermCandidateSummary
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.concurrent.atomic.AtomicLong

@Repository
class CandidateRepository {
    private val candidates = linkedMapOf<String, TermCandidate>()
    private val candidateSequence = AtomicLong(1)
    private val historySequence = AtomicLong(1)

    fun create(command: CandidateCommand, similarTerms: List<SimilarTerm>): TermCandidate {
        val next = candidateSequence.getAndIncrement()
        val candidateId = "CAND-%06d".format(next)
        val now = OffsetDateTime.now()
        val history = history(candidateId, CandidateStatus.Draft, "신규 후보 등록", command.requestedBy, now)
        val candidate = TermCandidate(
            candidateId = candidateId,
            koreanName = command.koreanName,
            englishName = command.englishName,
            englishAbbreviation = command.englishAbbreviation,
            domainName = command.domainName,
            businessDefinition = command.businessDefinition,
            usageContext = command.usageContext,
            physicalType = command.physicalType,
            digits = command.digits,
            decimalPoint = command.decimalPoint,
            status = CandidateStatus.Draft,
            requestedBy = command.requestedBy,
            similarTerms = similarTerms,
            histories = listOf(history),
            createdAt = now,
            updatedAt = now,
        )
        candidates[candidateId] = candidate
        return candidate
    }

    fun list(q: String?, status: CandidateStatus?, domainName: String?, page: Int, size: Int): TermCandidateListResponse {
        val normalized = q?.trim()?.lowercase()
        val filtered = candidates.values
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

    fun get(candidateId: String): TermCandidate = candidates[candidateId] ?: throw CandidateNotFoundException(candidateId)

    fun updateStatus(candidateId: String, status: CandidateStatus, actor: String, reason: String, reviewedBy: String? = null, promotedTermId: String? = null): TermCandidate {
        val existing = get(candidateId)
        val now = OffsetDateTime.now()
        val updated = existing.copy(
            status = status,
            reviewedBy = reviewedBy ?: existing.reviewedBy,
            promotedTermId = promotedTermId ?: existing.promotedTermId,
            histories = existing.histories + history(candidateId, status, reason, actor, now),
            updatedAt = now,
        )
        candidates[candidateId] = updated
        return updated
    }

    private fun history(candidateId: String, status: CandidateStatus, reason: String, actor: String, now: OffsetDateTime): CandidateHistory =
        CandidateHistory(
            historyId = "CAND-HIST-%06d".format(historySequence.getAndIncrement()),
            candidateId = candidateId,
            status = status,
            reason = reason,
            actor = actor,
            createdAt = now,
        )

    private fun TermCandidate.toSummary(): TermCandidateSummary = TermCandidateSummary(
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

data class CandidateCommand(
    val koreanName: String,
    val englishName: String,
    val englishAbbreviation: String,
    val domainName: String,
    val businessDefinition: String,
    val usageContext: String?,
    val physicalType: String,
    val digits: Int,
    val decimalPoint: Int,
    val requestedBy: String,
)
