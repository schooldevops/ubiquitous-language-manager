package com.aulms.persistence

import com.querydsl.sql.Configuration
import com.querydsl.sql.PostgreSQLTemplates
import com.querydsl.sql.SQLQueryFactory
import com.querydsl.sql.spring.SpringConnectionProvider
import com.querydsl.sql.spring.SpringExceptionTranslator
import com.querydsl.sql.types.JSR310InstantType
import com.querydsl.sql.types.JSR310LocalDateTimeType
import com.querydsl.sql.types.JSR310LocalDateType
import com.querydsl.sql.types.JSR310OffsetDateTimeType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration as SpringConfiguration
import org.springframework.context.annotation.Profile
import javax.sql.DataSource

/**
 * Querydsl-SQL 설정 (postgres 프로파일). JPA 미사용.
 * SpringConnectionProvider 가 트랜잭션 바운드 커넥션(@Transactional + DataSourceTransactionManager)을 제공한다.
 */
@SpringConfiguration
@Profile("postgres")
class QuerydslConfig {

    @Bean
    fun sqlQueryFactory(dataSource: DataSource): SQLQueryFactory {
        val configuration = Configuration(PostgreSQLTemplates.builder().build())
        configuration.setExceptionTranslator(SpringExceptionTranslator())
        // Querydsl-SQL 은 JSR310 시간 타입을 기본 등록하지 않으므로 수동 등록.
        configuration.register(JSR310OffsetDateTimeType())
        configuration.register(JSR310LocalDateTimeType())
        configuration.register(JSR310LocalDateType())
        configuration.register(JSR310InstantType())
        val provider = SpringConnectionProvider(dataSource)
        return SQLQueryFactory(configuration, provider)
    }
}
