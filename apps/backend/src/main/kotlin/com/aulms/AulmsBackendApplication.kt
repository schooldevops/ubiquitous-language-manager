package com.aulms

import org.springframework.ai.model.google.genai.autoconfigure.chat.GoogleGenAiChatAutoConfiguration
import org.springframework.ai.model.openai.autoconfigure.OpenAiAudioSpeechAutoConfiguration
import org.springframework.ai.model.openai.autoconfigure.OpenAiAudioTranscriptionAutoConfiguration
import org.springframework.ai.model.openai.autoconfigure.OpenAiChatAutoConfiguration
import org.springframework.ai.model.openai.autoconfigure.OpenAiEmbeddingAutoConfiguration
import org.springframework.ai.model.openai.autoconfigure.OpenAiImageAutoConfiguration
import org.springframework.ai.model.openai.autoconfigure.OpenAiModerationAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [
        GoogleGenAiChatAutoConfiguration::class,
        OpenAiChatAutoConfiguration::class,
        OpenAiAudioSpeechAutoConfiguration::class,
        OpenAiAudioTranscriptionAutoConfiguration::class,
        OpenAiEmbeddingAutoConfiguration::class,
        OpenAiImageAutoConfiguration::class,
        OpenAiModerationAutoConfiguration::class,
    ]
)
class AulmsBackendApplication

fun main(args: Array<String>) {
    runApplication<AulmsBackendApplication>(*args)
}

