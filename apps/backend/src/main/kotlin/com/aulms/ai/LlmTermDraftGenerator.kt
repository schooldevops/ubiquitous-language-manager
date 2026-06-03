package com.aulms.ai

import com.aulms.model.GraphRecommendationContext
import com.aulms.model.RecommendedTermDraft
import com.aulms.model.TermRecommendationRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class LlmTermDraftGenerator(
    private val chatModels: Map<String, ChatModel>,
    private val objectMapper: ObjectMapper,
    @Value("\${aulms.llm.provider:gemini}") private val provider: String,
    @Value("\${spring.ai.anthropic.api-key:}") private val anthropicKey: String,
    @Value("\${spring.ai.google.genai.api-key:}") private val geminiKey: String,
    @Value("\${spring.ai.openai.api-key:}") private val openaiKey: String,
) {
    fun generate(
        request: TermRecommendationRequest,
        inferredDomainName: String,
        graphContext: GraphRecommendationContext,
    ): RecommendedTermDraft {
        val (beanName, apiKey) = when (provider.lowercase()) {
            "anthropic"         -> "anthropicChatModel" to anthropicKey
            "openai", "chatgpt" -> "openAiChatModel"    to openaiKey
            "gemini", "google"  -> "googleGenAiChatModel" to geminiKey
            else -> throw LlmUnavailableException("알 수 없는 LLM provider: $provider")
        }
        if (apiKey.isBlank()) throw LlmUnavailableException("$provider api key 미설정")
        val chatModel = chatModels[beanName]
            ?: throw LlmUnavailableException("$provider ChatModel 빈 없음")

        val content = try {
            val response = chatModel.call(Prompt(buildPrompt(request, inferredDomainName, graphContext)))
            response.result?.output?.text ?: throw LlmUnavailableException("LLM 응답 본문 없음")
        } catch (ex: LlmUnavailableException) {
            throw ex
        } catch (ex: Exception) {
            throw LlmUnavailableException("LLM 호출 실패: ${ex.message}", ex)
        }
        val json = extractJson(content) ?: throw LlmUnavailableException("LLM 응답에서 JSON 추출 실패")
        val payload = try {
            objectMapper.readValue(json, LlmDraftPayload::class.java)
        } catch (ex: Exception) {
            throw LlmUnavailableException("LLM JSON 파싱 실패: ${ex.message}", ex)
        }
        return payload.toDraft()
    }

    private fun extractJson(text: String): String? {
        val start = text.indexOf('{')
        val end = text.lastIndexOf('}')
        return if (start >= 0 && end > start) text.substring(start, end + 1) else null
    }

    private fun buildPrompt(
        request: TermRecommendationRequest,
        inferredDomainName: String,
        graphContext: GraphRecommendationContext,
    ): String = """
        너는 데이터 표준 용어 사전 전문가다. 아래 한글 용어명에 대한 표준 용어 후보를 추론한다.
        한글명: ${request.koreanName}
        추정 도메인: $inferredDomainName
        관련 용어: ${graphContext.relatedTerms.joinToString(", ").ifBlank { "없음" }}
        관계 힌트: ${graphContext.relationshipHints.joinToString("; ").ifBlank { "없음" }}
        기존 업무정의(있으면 우선): ${request.currentBusinessDefinition ?: "없음"}
        기존 사용맥락(있으면 우선): ${request.currentUsageContext ?: "없음"}

        다음 JSON 스키마로만 답하라. 코드블록/설명 없이 순수 JSON 객체만 출력한다.
        {
          "domainName": string, "usageType": string,
          "englishName": string, "englishAbbreviation": string(대문자_언더스코어),
          "businessDefinition": string, "usageContext": string,
          "physicalType": string(VARCHAR|CHAR|NUMERIC|DATE|TIMESTAMP 등),
          "digits": number, "decimalPoint": number, "owner": string
        }
    """.trimIndent()

    private fun LlmDraftPayload.toDraft(): RecommendedTermDraft = RecommendedTermDraft(
        domainName = domainName,
        usageType = usageType,
        englishName = englishName,
        englishAbbreviation = englishAbbreviation,
        businessDefinition = businessDefinition,
        usageContext = usageContext,
        physicalType = physicalType,
        digits = digits,
        decimalPoint = decimalPoint,
        owner = owner,
    )
}
