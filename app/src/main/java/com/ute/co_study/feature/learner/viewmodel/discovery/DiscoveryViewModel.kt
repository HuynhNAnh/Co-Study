package com.ute.co_study.feature.learner.viewmodel.discovery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.MatchFilters
import com.ute.co_study.feature.learner.model.MatchLevel
import com.ute.co_study.feature.learner.model.StudyDay
import com.ute.co_study.feature.learner.model.StudyMode
import com.ute.co_study.feature.learner.model.WeeklyTimeSlot
import java.text.Normalizer
import java.util.Locale

class DiscoveryViewModel(application: Application) : AndroidViewModel(application) {
    private val accents = Regex("\\p{M}+")
    private val separators = Regex("[^\\p{L}\\p{N}]+")
    private val taughtSkills = normalize(application.getString(R.string.discovery_search_hint))
    private val sampleAvailability = WeeklyTimeSlot(StudyDay.THURSDAY, 19 * 60, 20 * 60)
    private val sampleKeywords = normalize(
        listOf(
            application.getString(R.string.home_suggested_name),
            application.getString(R.string.discovery_partner_skills),
            application.getString(R.string.discovery_search_hint)
        ).joinToString(" ")
    )

    fun matchesSample(query: String, filters: MatchFilters): Boolean =
        containsTerms(sampleKeywords, query) &&
            containsTerms(taughtSkills, filters.skill) &&
            filters.level != MatchLevel.ADVANCED &&
            (filters.mode == StudyMode.ANY || filters.mode == StudyMode.ONLINE) &&
            (filters.timeSlot == null || sampleAvailability.overlaps(filters.timeSlot))

    private fun containsTerms(source: String, query: String): Boolean = normalize(query)
        .split(separators)
        .filter(String::isNotEmpty)
        .all { it in source }

    private fun normalize(text: String): String =
        Normalizer.normalize(text.lowercase(Locale.ROOT), Normalizer.Form.NFD)
            .replace(accents, "")
            .replace('đ', 'd')
}
