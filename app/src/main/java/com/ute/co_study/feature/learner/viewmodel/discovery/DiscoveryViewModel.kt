package com.ute.co_study.feature.learner.viewmodel.discovery

import androidx.lifecycle.ViewModel
import com.ute.co_study.feature.learner.model.MatchFilters
import com.ute.co_study.feature.learner.model.MatchLevel
import com.ute.co_study.feature.learner.model.SamplePartner
import com.ute.co_study.feature.learner.model.SamplePartners
import com.ute.co_study.feature.learner.model.StudyMode
import com.ute.co_study.feature.learner.model.matchesLearnerSearch

class DiscoveryViewModel : ViewModel() {
    internal fun findPartners(query: String, filters: MatchFilters): List<SamplePartner> =
        SamplePartners.all.filter { partner ->
            val keywords = "${partner.conversation.title} ${partner.shares} ${partner.wants}"
            keywords.matchesLearnerSearch(query) &&
                partner.shares.matchesLearnerSearch(filters.skill) &&
                filters.level.accepts(partner.level) &&
                (filters.mode == StudyMode.ANY || filters.mode in partner.modes) &&
                (filters.timeSlot == null || partner.availability.any { it.overlaps(filters.timeSlot) })
        }

    private fun MatchLevel.accepts(level: MatchLevel): Boolean = when (this) {
        MatchLevel.ANY -> true
        MatchLevel.BEGINNER_TO_INTERMEDIATE -> level != MatchLevel.ADVANCED
        MatchLevel.BEGINNER, MatchLevel.INTERMEDIATE -> level == this || level == MatchLevel.BEGINNER_TO_INTERMEDIATE
        MatchLevel.ADVANCED -> level == this
    }
}
