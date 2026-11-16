package com.ute.co_study.feature.learner.ui.discovery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import com.ute.co_study.feature.learner.model.MatchFilters
import com.ute.co_study.feature.learner.model.MatchLevel
import com.ute.co_study.feature.learner.model.StudyDay
import com.ute.co_study.feature.learner.model.StudyMode
import com.ute.co_study.feature.learner.model.WeeklyTimeSlot

internal class MatchFilterContract : ActivityResultContract<MatchFilters, MatchFilters?>() {
    override fun createIntent(context: Context, input: MatchFilters): Intent =
        Intent(context, MatchFiltersActivity::class.java).putExtra(FILTERS, input.toBundle())

    override fun parseResult(resultCode: Int, intent: Intent?): MatchFilters? =
        if (resultCode == Activity.RESULT_OK) readFilters(intent) else null

    companion object {
        private const val FILTERS = "match_filters"

        fun readFilters(intent: Intent?): MatchFilters? = intent?.getBundleExtra(FILTERS)?.toMatchFilters()

        fun result(filters: MatchFilters): Intent = Intent().putExtra(FILTERS, filters.toBundle())
    }
}

internal fun defaultMatchFilters() = MatchFilters(
    skill = "",
    level = MatchLevel.ANY,
    mode = StudyMode.ANY,
    timeSlot = null
)

internal fun MatchFilters.toBundle() = Bundle().apply {
    putString("skill", skill)
    putString("level", level.name)
    putString("mode", mode.name)
    timeSlot?.let { slot ->
        putString("day", slot.day.name)
        putInt("start", slot.startMinute)
        putInt("end", slot.endMinute)
    }
}

internal fun Bundle.toMatchFilters(): MatchFilters {
    val day = getString("day")
    return MatchFilters(
        skill = getString("skill").orEmpty(),
        level = getString("level")?.let(MatchLevel::valueOf) ?: MatchLevel.ANY,
        mode = getString("mode")?.let(StudyMode::valueOf) ?: StudyMode.ANY,
        timeSlot = day?.let { WeeklyTimeSlot(StudyDay.valueOf(it), getInt("start"), getInt("end")) }
    )
}
