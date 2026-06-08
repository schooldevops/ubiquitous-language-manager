package com.aulms.persistence

import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

/** postgres 시퀀스에서 다음 ID 값을 가져온다. memory seed 와 ID 포맷을 맞추기 위해 사용. */
@Component
@Profile("postgres")
class PostgresSequences(private val jdbc: JdbcTemplate) {
    fun next(sequence: String): Long =
        jdbc.queryForObject("SELECT nextval(?)", Long::class.java, sequence)
            ?: error("nextval returned null for sequence $sequence")
}
