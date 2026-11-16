package com.ute.co_study.feature.learner.viewmodel.discovery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ute.co_study.R
import java.text.Normalizer
import java.util.Locale

class DiscoveryViewModel(application: Application) : AndroidViewModel(application) {
    private val accents = Regex("\\p{M}+")
    private val separators = Regex("[^\\p{L}\\p{N}]+")
    private val sampleKeywords = normalize(
        listOf(
            application.getString(R.string.home_suggested_name),
            application.getString(R.string.discovery_partner_skills),
            application.getString(R.string.discovery_search_hint)
        ).joinToString(" ")
    )

    fun matchesSample(query: String): Boolean = normalize(query)
        .split(separators)
        .filter(String::isNotEmpty)
        .all { it in sampleKeywords }

    private fun normalize(text: String): String =
        Normalizer.normalize(text.lowercase(Locale.ROOT), Normalizer.Form.NFD)
            .replace(accents, "")
            .replace('đ', 'd')
}
