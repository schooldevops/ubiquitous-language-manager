package com.aulms.search

object SearchNormalizer {
    private val separators = Regex("[\\s_-]+")

    fun normalize(value: String): String = value
        .trim()
        .replace(separators, "")
        .lowercase()
}
