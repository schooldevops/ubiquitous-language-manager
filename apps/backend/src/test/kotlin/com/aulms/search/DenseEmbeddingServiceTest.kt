package com.aulms.search

import org.junit.jupiter.api.Test
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.embedding.EmbeddingResponse
import org.springframework.ai.embedding.Embedding
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DenseEmbeddingServiceTest {
    private fun modelReturning(vararg vectors: FloatArray): EmbeddingModel =
        object : EmbeddingModel {
            var i = 0
            override fun call(request: org.springframework.ai.embedding.EmbeddingRequest): EmbeddingResponse {
                val out = request.instructions.map { Embedding(vectors[i++], 0) }
                return EmbeddingResponse(out)
            }
            override fun embed(document: org.springframework.ai.document.Document): FloatArray =
                throw UnsupportedOperationException("not used in these tests")
        }

    @Test
    fun `embed delegates to model and returns vector`() {
        val service = DenseEmbeddingService(modelReturning(floatArrayOf(1f, 0f, 0f)))
        val v = service.embed("주문일자")
        assertEquals(listOf(1f, 0f, 0f), v.toList())
    }

    @Test
    fun `cosine of identical vectors is 1`() {
        val service = DenseEmbeddingService(modelReturning())
        val a = floatArrayOf(1f, 2f, 2f)
        assertEquals(1.0, service.cosine(a, a), 1e-9)
    }

    @Test
    fun `cosine of orthogonal vectors is 0`() {
        val service = DenseEmbeddingService(modelReturning())
        assertEquals(0.0, service.cosine(floatArrayOf(1f, 0f), floatArrayOf(0f, 1f)), 1e-9)
    }

    @Test
    fun `cosine handles zero vector without NaN`() {
        val service = DenseEmbeddingService(modelReturning())
        assertTrue(service.cosine(floatArrayOf(0f, 0f), floatArrayOf(1f, 1f)) == 0.0)
    }

    @Test
    fun `cosine of mismatched-length arrays returns 0`() {
        val service = DenseEmbeddingService(modelReturning())
        assertEquals(0.0, service.cosine(floatArrayOf(1f, 0f), floatArrayOf(1f, 0f, 0f)), 1e-9)
    }
}
