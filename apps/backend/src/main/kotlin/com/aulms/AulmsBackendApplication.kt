package com.aulms

import org.springframework.ai.model.google.genai.autoconfigure.chat.GoogleGenAiChatAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [GoogleGenAiChatAutoConfiguration::class])
class AulmsBackendApplication

fun main(args: Array<String>) {
    runApplication<AulmsBackendApplication>(*args)
}

