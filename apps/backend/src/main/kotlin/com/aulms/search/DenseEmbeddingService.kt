package com.aulms.search

import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.stereotype.Component
import kotlin.math.sqrt

@Component
class DenseEmbeddingService(private val embeddingModel: EmbeddingModel) {
    fun embed(text: String): FloatArray = embeddingModel.embed(text)

    fun cosine(left: FloatArray, right: FloatArray): Double {
        if (left.isEmpty() || right.isEmpty() || left.size != right.size) return 0.0
        var dot = 0.0
        var leftNorm = 0.0
        var rightNorm = 0.0
        for (i in left.indices) {
            dot += left[i].toDouble() * right[i].toDouble()
            leftNorm += left[i].toDouble() * left[i].toDouble()
            rightNorm += right[i].toDouble() * right[i].toDouble()
        }
        return if (leftNorm == 0.0 || rightNorm == 0.0) 0.0 else dot / (sqrt(leftNorm) * sqrt(rightNorm))
    }
}
