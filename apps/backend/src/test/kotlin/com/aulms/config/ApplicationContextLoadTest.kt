package com.aulms.config

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApplicationContextLoadTest {
    @Test
    fun `context loads without anthropic api key`() {
        // ANTHROPIC_API_KEY 미설정 상태에서 컨텍스트가 정상 기동해야 폴백 경로가 보장됨
    }
}
