package com.aulms.search

import com.aulms.term.TermRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/** memory 프로파일: 매 질의마다 전체 용어를 임베딩해 코사인 계산. */
@Component
@Profile("!postgres")
class InMemoryTermVectorIndex(
    private val termRepository: TermRepository,
    private val embeddingService: DenseEmbeddingService,
    private val vectorIndex: SemanticVectorIndex,
) : TermVectorIndex {
    override fun similarities(query: String): List<TermVectorHit> {
        val queryEmbedding = embeddingService.embed(query)
        return vectorIndex.build(termRepository.searchDocuments())
            .map { TermVectorHit(it.document, embeddingService.cosine(queryEmbedding, it.embedding)) }
    }
}
