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

    /** Build a generator that uses Gemini (default provider) with a given mock model and key. */
    private fun geminiGenerator(model: ChatModel, geminiKey: String = "test-key") =
        LlmTermDraftGenerator(
            chatModels = mapOf("googleGenAiChatModel" to model),
            objectMapper = jacksonObjectMapper(),
            provider = "gemini",
            anthropicKey = "",
            geminiKey = geminiKey,
            openaiKey = "",
        )

    @Test
    fun `parses valid json into RecommendedTermDraft`() {
        val json = """
            {"domainName":"주문","usageType":"표준항목","englishName":"Order Cancel Reason Code",
             "englishAbbreviation":"ORD_CNCL_RSN_CD","businessDefinition":"주문 취소 사유를 코드로 관리",
             "usageContext":"주문 취소 화면/API","physicalType":"VARCHAR","digits":10,"decimalPoint":0,
             "owner":"주문도메인 데이터스튜어드"}
        """.trimIndent()
        val draft: RecommendedTermDraft = geminiGenerator(modelReturning(json)).generate(request, "주문", graphContext)
        assertEquals("Order Cancel Reason Code", draft.englishName)
        assertEquals("ORD_CNCL_RSN_CD", draft.englishAbbreviation)
        assertEquals(10, draft.digits)
        assertEquals("주문", draft.domainName)
        assertEquals(0, draft.decimalPoint)
    }

    @Test
    fun `throws when api key blank`() {
        assertThrows<LlmUnavailableException> {
            geminiGenerator(modelReturning("{}"), geminiKey = "").generate(request, "주문", graphContext)
        }
    }

    @Test
    fun `throws when model returns non-json`() {
        assertThrows<LlmUnavailableException> {
            geminiGenerator(modelReturning("죄송합니다 모르겠어요")).generate(request, "주문", graphContext)
        }
    }

    @Test
    fun `throws when model call fails`() {
        val failing: ChatModel = mock {
            on { call(any<Prompt>()) } doThrow RuntimeException("upstream 500")
        }
        assertThrows<LlmUnavailableException> {
            geminiGenerator(failing).generate(request, "주문", graphContext)
        }
    }

    @Test
    fun `selects openai bean when provider is openai`() {
        val json = """
            {"domainName":"주문","usageType":"표준항목","englishName":"Order Cancel Reason Code",
             "englishAbbreviation":"ORD_CNCL_RSN_CD","businessDefinition":"주문 취소 사유를 코드로 관리",
             "usageContext":"주문 취소 화면/API","physicalType":"VARCHAR","digits":10,"decimalPoint":0,
             "owner":"주문도메인 데이터스튜어드"}
        """.trimIndent()
        val openAiMock = modelReturning(json)
        val generator = LlmTermDraftGenerator(
            chatModels = mapOf("openAiChatModel" to openAiMock),
            objectMapper = jacksonObjectMapper(),
            provider = "openai",
            anthropicKey = "",
            geminiKey = "",
            openaiKey = "sk-test-key",
        )
        val draft = generator.generate(request, "주문", graphContext)
        assertEquals("Order Cancel Reason Code", draft.englishName)
    }

    @Test
    fun `throws LlmUnavailableException for unknown provider`() {
        val generator = LlmTermDraftGenerator(
            chatModels = emptyMap(),
            objectMapper = jacksonObjectMapper(),
            provider = "unknown-provider",
            anthropicKey = "",
            geminiKey = "",
            openaiKey = "",
        )
        assertThrows<LlmUnavailableException> {
            generator.generate(request, "주문", graphContext)
        }
    }
}
