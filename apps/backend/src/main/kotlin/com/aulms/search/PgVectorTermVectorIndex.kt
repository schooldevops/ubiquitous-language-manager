package com.aulms.search

import com.aulms.term.TermRepository
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * postgres 프로파일: term 임베딩을 pgvector(term_embedding) 에 저장하고
 * 코사인 거리 연산자(<=>)로 유사도를 계산한다. cosine = 1 - distance.
 * 임베딩 텍스트 구성은 [SemanticVectorIndex.contentOf] 를 공유해 memory 모드와 점수 호환.
 */
@Component
@Profile("postgres")
class PgVectorTermVectorIndex(
    private val termRepository: TermRepository,
    private val embeddingService: DenseEmbeddingService,
    private val vectorIndex: SemanticVectorIndex,
    private val jdbc: JdbcTemplate,
) : TermVectorIndex {

    @Transactional
    override fun similarities(query: String): List<TermVectorHit> {
        val documents = termRepository.searchDocuments().associateBy { it.term.termId }
        ensurePopulated(documents.size)
        val queryVector = vectorLiteral(embeddingService.embed(query))
        val rows = jdbc.query(
            "SELECT term_id, 1 - (embedding <=> ?::vector) AS cosine FROM term_embedding",
            { rs, _ -> rs.getString("term_id") to rs.getDouble("cosine") },
            queryVector,
        )
        return rows.mapNotNull { (termId, cosine) -> documents[termId]?.let { TermVectorHit(it, cosine) } }
    }

    /** term 수와 임베딩 행 수가 다르면 전체 재적재(dev 단순화). */
    private fun ensurePopulated(termCount: Int) {
        val stored = jdbc.queryForObject("SELECT count(*) FROM term_embedding", Long::class.java) ?: 0L
        if (stored.toInt() == termCount) return
        jdbc.update("DELETE FROM term_embedding")
        termRepository.searchDocuments().forEach { document ->
            val embedding = embeddingService.embed(vectorIndex.contentOf(document))
            jdbc.update(
                "INSERT INTO term_embedding(term_id, embedding) VALUES (?, ?::vector)",
                document.term.termId,
                vectorLiteral(embedding),
            )
        }
    }

    private fun vectorLiteral(embedding: FloatArray): String =
        embedding.joinToString(prefix = "[", postfix = "]", separator = ",") { it.toString() }
}
