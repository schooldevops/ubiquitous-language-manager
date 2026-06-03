package com.aulms.search

import com.aulms.term.TermSearchDocument
import org.springframework.stereotype.Component

@Component
class SemanticVectorIndex(private val embeddingService: DenseEmbeddingService) {
    fun build(documents: List<TermSearchDocument>): List<SemanticVectorDocument> =
        documents.map { document ->
            val content = listOf(
                document.term.koreanName,
                document.term.englishName,
                document.term.englishAbbreviation,
                document.term.businessDefinition,
                document.term.usageContext.orEmpty(),
                document.expressions.joinToString(" ") { it.expressionValue },
                document.aliases.joinToString(" ") { "${it.aliasName} ${it.reason}" },
            ).joinToString(" ")
            SemanticVectorDocument(
                document = document,
                content = content,
                embedding = embeddingService.embed(content),
            )
        }
}

data class SemanticVectorDocument(
    val document: TermSearchDocument,
    val content: String,
    val embedding: FloatArray,
)
