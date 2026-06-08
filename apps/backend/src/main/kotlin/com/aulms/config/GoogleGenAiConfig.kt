package com.aulms.config

import com.google.genai.Client
import org.springframework.ai.google.genai.GoogleGenAiChatModel
import org.springframework.ai.model.google.genai.autoconfigure.chat.GoogleGenAiChatProperties
import org.springframework.ai.model.tool.DefaultToolExecutionEligibilityPredicate
import org.springframework.ai.model.tool.ToolCallingManager
import org.springframework.ai.model.tool.ToolExecutionEligibilityPredicate
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.core.type.AnnotatedTypeMetadata
import org.springframework.retry.support.RetryTemplate
import org.springframework.util.StringUtils

/**
 * Condition that passes only when spring.ai.google.genai.api-key resolves to
 * a non-blank string (StringUtils.hasText). This is stricter than
 * @ConditionalOnProperty which matches even an empty-string value.
 */
class GeminiApiKeyPresentCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val key = context.environment.getProperty("spring.ai.google.genai.api-key") ?: return false
        return StringUtils.hasText(key)
    }
}

/**
 * Conditional Google GenAI configuration.
 *
 * The standard GoogleGenAiChatAutoConfiguration throws IllegalStateException when
 * neither api-key nor Vertex AI project/location is configured. This custom config
 * replaces it: beans are created only when the Gemini api-key is non-blank, so
 * the app boots keyless (heuristic-fallback path) without error.
 *
 * Paired with: @SpringBootApplication(exclude = [GoogleGenAiChatAutoConfiguration::class])
 */
@Configuration
@EnableConfigurationProperties(GoogleGenAiChatProperties::class)
class GoogleGenAiConfig {

    @Bean
    @Conditional(GeminiApiKeyPresentCondition::class)
    fun googleGenAiClient(
        @Value("\${spring.ai.google.genai.api-key:}") apiKey: String,
    ): Client = Client.builder().apiKey(apiKey).build()

    @Bean
    @Conditional(GeminiApiKeyPresentCondition::class)
    fun googleGenAiChatModel(
        googleGenAiClient: Client,
        chatProperties: GoogleGenAiChatProperties,
        toolCallingManager: ToolCallingManager,
        context: ApplicationContext,
        retryTemplate: RetryTemplate,
        toolExecutionEligibilityPredicate: ObjectProvider<ToolExecutionEligibilityPredicate>,
    ): GoogleGenAiChatModel = GoogleGenAiChatModel.builder()
        .genAiClient(googleGenAiClient)
        .defaultOptions(chatProperties.options)
        .toolCallingManager(toolCallingManager)
        .toolExecutionEligibilityPredicate(
            toolExecutionEligibilityPredicate.getIfUnique { DefaultToolExecutionEligibilityPredicate() }
        )
        .retryTemplate(retryTemplate)
        .build()
}
