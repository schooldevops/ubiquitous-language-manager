package com.aulms.config

import org.springframework.boot.web.client.ClientHttpRequestFactories
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings
import org.springframework.boot.web.client.RestClientCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

/**
 * 로컬 Ollama 등 느린 LLM 호출용 RestClient 타임아웃 확장.
 *
 * Spring AI Ollama 의 OllamaApi 는 부트가 구성한 RestClient.Builder 로 /api/chat 동기 호출을 한다.
 * gemma2:9b 같은 로컬 모델은 첫 호출 시 모델 로딩+추론에 수십 초~수 분이 걸려 기본 read timeout 을 초과한다.
 * 이 커스터마이저로 read timeout 을 넉넉히 늘려 timeout 예외를 막는다.
 */
@Configuration
class HttpClientConfig {

    @Bean
    fun longTimeoutRestClientCustomizer(): RestClientCustomizer = RestClientCustomizer { builder ->
        val settings = ClientHttpRequestFactorySettings.DEFAULTS
            .withConnectTimeout(Duration.ofSeconds(30))
            .withReadTimeout(Duration.ofMinutes(5))
        builder.requestFactory(ClientHttpRequestFactories.get(settings))
    }
}
