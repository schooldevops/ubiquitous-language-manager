package com.aulms.search

import com.aulms.term.TermSearchDocument

/** 질의에 대한 용어 문서별 코사인 유사도. 영속화 모드별 구현(in-memory / pgvector). */
interface TermVectorIndex {
    fun similarities(query: String): List<TermVectorHit>
}

data class TermVectorHit(
    val document: TermSearchDocument,
    val cosine: Double,
)
