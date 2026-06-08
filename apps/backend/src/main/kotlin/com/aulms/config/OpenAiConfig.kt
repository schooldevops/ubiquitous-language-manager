package com.aulms.config

import org.springframework.ai.model.openai.autoconfigure.OpenAiChatProperties
import org.springframework.ai.model.openai.autoconfigure.OpenAiConnectionProperties
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.core.type.AnnotatedTypeMetadata
import org.springframework.ai.model.tool.DefaultToolExecutionEligibilityPredicate
import org.springframework.ai.model.tool.ToolCallingManager
import org.springframework.ai.model.tool.ToolExecutionEligibilityPredicate
import org.springframework.retry.support.RetryTemplate
import org.springframework.util.StringUtils

/**
 * Condition that passes only when spring.ai.openai.api-key resolves to
 * a non-blank string (StringUtils.hasText). Mirrors GeminiApiKeyPresentCondition.
 */
class OpenAiApiKeyPresentCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val key = context.environment.getProperty("spring.ai.openai.api-key") ?: return false
        return StringUtils.hasText(key)
    }
}

/**
 * Conditional OpenAI configuration.
 *
 * The standard OpenAiChatAutoConfiguration calls Assert.hasText on the api-key and throws
 * IllegalArgumentException when the key is blank. This custom config replaces it: beans are
 * created only when the OpenAI api-key is non-blank, so the app boots keyless
 * (heuristic-fallback path) without error.
 *
 * Paired with: @SpringBootApplication(exclude = [OpenAiChatAutoConfiguration::class])
 */
@Configuration
@EnableConfigurationProperties(OpenAiConnectionProperties::class, OpenAiChatProperties::class)
class OpenAiConfig {

    @Bean
    @Conditional(OpenAiApiKeyPresentCondition::class)
    fun openAiApi(
        @Value("\${spring.ai.openai.api-key:}") apiKey: String,
    ): OpenAiApi = OpenAiApi.builder()
        .apiKey(apiKey)
        .build()

    @Bean
    @Conditional(OpenAiApiKeyPresentCondition::class)
    fun openAiChatModel(
        openAiApi: OpenAiApi,
        chatProperties: OpenAiChatProperties,
        toolCallingManager: ToolCallingManager,
        retryTemplate: RetryTemplate,
        toolExecutionEligibilityPredicate: ObjectProvider<ToolExecutionEligibilityPredicate>,
    ): OpenAiChatModel = OpenAiChatModel.builder()
        .openAiApi(openAiApi)
        .defaultOptions(chatProperties.options)
        .toolCallingManager(toolCallingManager)
        .toolExecutionEligibilityPredicate(
            toolExecutionEligibilityPredicate.getIfUnique { DefaultToolExecutionEligibilityPredicate() }
        )
        .retryTemplate(retryTemplate)
        .build()
}
