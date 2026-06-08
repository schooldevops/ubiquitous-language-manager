package com.aulms.search

import com.aulms.term.TermSearchDocument
import org.springframework.stereotype.Component

@Component
class SemanticVectorIndex(private val embeddingService: DenseEmbeddingService) {
    fun build(documents: List<TermSearchDocument>): List<SemanticVectorDocument> =
        documents.map { document ->
            val content = contentOf(document)
            SemanticVectorDocument(
                document = document,
                content = content,
                embedding = embeddingService.embed(content),
            )
        }

    /** 임베딩 입력 텍스트 구성. memory/pgvector 두 백엔드가 동일 텍스트를 써 점수가 비교 가능하도록 공유. */
    fun contentOf(document: TermSearchDocument): String = listOf(
        document.term.koreanName,
        document.term.englishName,
        document.term.englishAbbreviation,
        document.term.businessDefinition,
        document.term.usageContext.orEmpty(),
        document.expressions.joinToString(" ") { it.expressionValue },
        document.aliases.joinToString(" ") { "${it.aliasName} ${it.reason}" },
    ).joinToString(" ")
}

data class SemanticVectorDocument(
    val document: TermSearchDocument,
    val content: String,
    val embedding: FloatArray,
)
