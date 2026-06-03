package com.aulms.ai

import com.aulms.model.GraphRecommendationContext
import com.aulms.model.RecommendedTermDraft
import com.aulms.model.TermRecommendationRequest
import com.aulms.model.TermRecommendationMode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.model.Generation
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.prompt.Prompt
import kotlin.test.assertEquals

class LlmTermDraftGeneratorTest {
    private val request = TermRecommendationRequest(
        koreanName = "주문취소사유코드",
        mode = TermRecommendationMode.TERM_CREATE,
        currentDomainName = "주문",
    )
    private val graphContext = GraphRecommendationContext(
        inferredDomainName = "주문",
        relatedTerms = listOf("주문상태코드"),
        relationshipHints = listOf("주문 도메인 코드 체계와 함께 검토"),
    )

    private fun modelReturning(text: String): ChatModel = mock {
        on { call(any<Prompt>()) } doReturn ChatResponse(listOf(Generation(AssistantMessage(text))))
    }

    private fun generator(model: ChatModel, key: String = "test-key") =
        LlmTermDraftGenerator(model, jacksonObjectMapper(), key)

    @Test
    fun `parses valid json into RecommendedTermDraft`() {
        val json = """
            {"domainName":"주문","usageType":"표준항목","englishName":"Order Cancel Reason Code",
             "englishAbbreviation":"ORD_CNCL_RSN_CD","businessDefinition":"주문 취소 사유를 코드로 관리",
             "usageContext":"주문 취소 화면/API","physicalType":"VARCHAR","digits":10,"decimalPoint":0,
             "owner":"주문도메인 데이터스튜어드"}
        """.trimIndent()
        val draft: RecommendedTermDraft = generator(modelReturning(json)).generate(request, "주문", graphContext)
        assertEquals("Order Cancel Reason Code", draft.englishName)
        assertEquals("ORD_CNCL_RSN_CD", draft.englishAbbreviation)
        assertEquals(10, draft.digits)
        assertEquals("주문", draft.domainName)
        assertEquals(0, draft.decimalPoint)
    }

    @Test
    fun `throws when api key blank`() {
        assertThrows<LlmUnavailableException> {
            generator(modelReturning("{}"), key = "").generate(request, "주문", graphContext)
        }
    }

    @Test
    fun `throws when model returns non-json`() {
        assertThrows<LlmUnavailableException> {
            generator(modelReturning("죄송합니다 모르겠어요")).generate(request, "주문", graphContext)
        }
    }

    @Test
    fun `throws when model call fails`() {
        val failing: ChatModel = mock {
            on { call(any<Prompt>()) } doThrow RuntimeException("upstream 500")
        }
        assertThrows<LlmUnavailableException> {
            generator(failing).generate(request, "주문", graphContext)
        }
    }
}
