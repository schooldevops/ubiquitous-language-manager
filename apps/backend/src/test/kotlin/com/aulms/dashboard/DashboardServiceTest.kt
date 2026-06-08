package com.aulms.dashboard

import com.aulms.model.TermStatus
import com.aulms.term.InMemoryTermRepository
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DashboardServiceTest {
    // 시드 데이터(9개, 고객 3 / 주문 6, 전부 Approved)를 가진 인메모리 저장소로 결정적 검증.
    private val service = DashboardService(InMemoryTermRepository())

    @Test
    fun `totalTerms equals seeded term count`() {
        assertEquals(9, service.getDashboard(10).totalTerms)
    }

    @Test
    fun `domain stats aggregate counts per domain and status`() {
        val stats = service.getDashboard(10).domainStats.associateBy { it.domainName }
        val customer = assertNotNull(stats["고객"])
        assertEquals(3, customer.totalCount)
        assertEquals(3, customer.approvedCount)
        assertEquals(0, customer.draftCount)
        val order = assertNotNull(stats["주문"])
        assertEquals(6, order.totalCount)
        assertEquals(6, order.approvedCount)
    }

    @Test
    fun `recent terms are limited and carry dates`() {
        val recent = service.getDashboard(3).recentTerms
        assertEquals(3, recent.size)
        recent.forEach { assertNotNull(it.createdAt) }
    }

    @Test
    fun `recent terms sorted by updatedAt descending`() {
        val recent = service.getDashboard(10).recentTerms
        val updatedAts = recent.map { it.updatedAt }
        assertEquals(updatedAts.sortedDescending(), updatedAts)
    }

    @Test
    fun `domain stats sorted by total count descending`() {
        val totals = service.getDashboard(10).domainStats.map { it.totalCount }
        assertEquals(totals.sortedDescending(), totals)
        assertTrue(service.getDashboard(10).domainStats.first().domainName == "주문")
    }
}
