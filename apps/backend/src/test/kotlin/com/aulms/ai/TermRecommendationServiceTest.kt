package com.aulms.ai

import com.aulms.model.RecommendedTermDraft
import com.aulms.model.TermRecommendationMode
import com.aulms.model.TermRecommendationRequest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
class TermRecommendationServiceTest {
    @Autowired lateinit var service: TermRecommendationService
    @MockBean lateinit var generator: LlmTermDraftGenerator

    private fun req(name: String) = TermRecommendationRequest(
        koreanName = name,
        mode = TermRecommendationMode.TERM_CREATE,
        currentDomainName = "주문",
    )

    @Test
    fun `uses llm result when generator succeeds`() {
        val llmDraft = RecommendedTermDraft(
            domainName = "주문", usageType = "표준항목",
            englishName = "Order Magic Field", englishAbbreviation = "ORD_MAGIC",
            businessDefinition = "정의", usageContext = "맥락",
            physicalType = "VARCHAR", digits = 10, decimalPoint = 0, owner = "owner",
        )
        whenever(generator.generate(any(), any(), any())).thenReturn(llmDraft)
        val response = service.recommend(req("주문임의신규항목"))
        assertEquals("Order Magic Field", response.recommendation.englishName)
    }

    @Test
    fun `falls back to heuristic when generator throws`() {
        whenever(generator.generate(any(), any(), any())).thenThrow(LlmUnavailableException("no key"))
        val response = service.recommend(req("주문임의신규항목"))
        assertTrue(response.recommendation.englishAbbreviation.startsWith("ORD"))
        assertTrue(response.warnings.any { it.contains("LLM 미사용") })
    }

    @Test
    fun `skips llm and reuses existing term on exact match`() {
        val response = service.recommend(req("주문일자"))
        org.mockito.Mockito.verify(generator, org.mockito.Mockito.never()).generate(any(), any(), any())
        assertEquals("주문", response.recommendation.domainName)
    }
}
