package com.aulms.search

import org.springframework.stereotype.Component
import kotlin.math.sqrt

@Component
class LocalEmbeddingService {
    fun embed(text: String): Map<String, Double> {
        val expanded = normalizeTerms(text)
        val tokens = tokenPattern.findAll(expanded)
            .map { it.value.lowercase() }
            .flatMap { token -> tokenAliases[token]?.plus(token) ?: listOf(token) }
            .filter { it.length >= 2 && it !in stopWords }
            .toList()
        return tokens.groupingBy { it }.eachCount().mapValues { (_, count) -> count.toDouble() }
    }

    fun cosine(left: Map<String, Double>, right: Map<String, Double>): Double {
        if (left.isEmpty() || right.isEmpty()) return 0.0
        val dot = left.entries.sumOf { (token, value) -> value * (right[token] ?: 0.0) }
        val leftNorm = sqrt(left.values.sumOf { it * it })
        val rightNorm = sqrt(right.values.sumOf { it * it })
        return if (leftNorm == 0.0 || rightNorm == 0.0) 0.0 else dot / (leftNorm * rightNorm)
    }

    private fun normalizeTerms(text: String): String = text
        .replace("일시", "일시 날짜 시각 시간")
        .replace("일자", "일자 날짜")
        .replace("날짜", "날짜 일자")
        .replace("시각", "시각 시간 일시")
        .replace("발생", "발생 생성")

    private companion object {
        val tokenPattern = Regex("[가-힣A-Za-z0-9]+")
        val stopWords = setOf("하는", "에서", "으로", "사용", "기준", "대한", "값", "코드", "회사", "업무", "적으로")
        val tokenAliases = mapOf(
            "주문이" to listOf("주문"),
            "발생한" to listOf("발생", "생성"),
            "날짜와" to listOf("날짜", "일자"),
            "시분초" to listOf("시간", "시각", "일시"),
        )
    }
}
