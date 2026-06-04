package com.aulms.upload

import com.aulms.upload.sql.QuerydslTermUpsertRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/** 한 row 의 term 본문 upsert 를 독립 트랜잭션으로 처리(한 줄 실패가 다른 줄을 롤백하지 않도록). */
@Component
@Profile("postgres")
class TermUploadRowProcessor(private val upsert: QuerydslTermUpsertRepository) {

    /** 반환: true=INSERTED, false=UPDATED. 예외 시 호출측이 FAILED 처리. */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun upsertTermBody(term: ParsedTerm): Boolean {
        val inserted = upsert.upsertTerm(term)
        term.expressions.forEach { upsert.upsertExpression(it) }
        term.aliases.forEach { upsert.upsertAlias(it) }
        return inserted
    }

    /** 관계 upsert(독립 트랜잭션). 실패 시 예외 전파. */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun upsertRelationships(termId: String, relationships: List<ParsedRelationship>) {
        relationships.forEach { upsert.upsertRelationship(termId, it) }
    }
}
