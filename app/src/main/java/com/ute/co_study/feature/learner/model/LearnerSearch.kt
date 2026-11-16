package com.ute.co_study.feature.learner.model

import java.text.Normalizer
import java.util.Locale

private val accents = Regex("\\p{M}+")
private val separators = Regex("[^\\p{L}\\p{N}+#]+")

internal fun String.matchesLearnerSearch(query: String): Boolean {
    val searchable = normalizeSearch(this)
    return normalizeSearch(query).split(separators).filter(String::isNotEmpty).all { it in searchable }
}

private fun normalizeSearch(text: String): String =
    Normalizer.normalize(text.lowercase(Locale.ROOT), Normalizer.Form.NFD)
        .replace(accents, "")
        .replace('đ', 'd')
