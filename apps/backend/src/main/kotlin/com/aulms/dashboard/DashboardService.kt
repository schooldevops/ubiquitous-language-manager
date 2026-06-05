package com.aulms.dashboard

import com.aulms.model.DashboardSummary
import com.aulms.model.DomainTermStat
import com.aulms.model.RecentTermItem
import com.aulms.model.Term
import com.aulms.model.TermStatus
import com.aulms.term.TermRepository
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class DashboardService(
    private val repository: TermRepository,
) {
    fun getDashboard(recentLimit: Int): DashboardSummary {
        val terms = repository.searchDocuments().map { it.term }

        val recentTerms = terms
            .sortedByDescending { it.sortTimestamp() }
            .take(recentLimit)
            .map { term ->
                RecentTermItem(
                    termId = term.termId,
                    koreanName = term.koreanName,
                    englishName = term.englishName,
                    domainName = term.domainName,
                    status = term.status,
                    createdAt = term.createdAt ?: term.sortTimestamp(),
                    updatedAt = term.updatedAt ?: term.sortTimestamp(),
                )
            }

        val domainStats = terms
            .groupBy { it.domainName }
            .map { (domainName, domainTerms) ->
                DomainTermStat(
                    domainName = domainName,
                    totalCount = domainTerms.size,
                    approvedCount = domainTerms.count { it.status == TermStatus.Approved },
                    draftCount = domainTerms.count { it.status == TermStatus.Draft },
                    reviewingCount = domainTerms.count { it.status == TermStatus.Reviewing },
                    deprecatedCount = domainTerms.count { it.status == TermStatus.Deprecated },
                    rejectedCount = domainTerms.count { it.status == TermStatus.Rejected },
                )
            }
            .sortedWith(compareByDescending<DomainTermStat> { it.totalCount }.thenBy { it.domainName })

        return DashboardSummary(
            totalTerms = terms.size,
            recentTerms = recentTerms,
            domainStats = domainStats,
        )
    }

    private fun Term.sortTimestamp(): OffsetDateTime =
        updatedAt ?: createdAt ?: OffsetDateTime.MIN
}
