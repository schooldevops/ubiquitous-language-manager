package com.aulms

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityScheme

@Configuration
class SpringDocConfiguration {

    @Bean
    fun apiInfo(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("AULMS API")
                    .description("데이터 사전 기반 유비쿼터스 랭기지 관리 시스템 API")
                    .contact(
                        Contact()
                            .name("AULMS Team")
                    )
                    .version("0.1.0")
            )
            .components(
                Components()
                    .addSecuritySchemes("bearerAuth", SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                    )
            )
    }
}
