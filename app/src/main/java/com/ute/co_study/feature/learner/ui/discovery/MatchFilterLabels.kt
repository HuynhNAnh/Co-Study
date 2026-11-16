package com.ute.co_study.feature.learner.ui.discovery

import android.content.Context
import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.MatchLevel
import com.ute.co_study.feature.learner.model.StudyMode
import com.ute.co_study.feature.learner.model.WeeklyTimeSlot
import java.util.Locale

internal fun Context.levelLabels(): Array<String> = resources.getStringArray(R.array.match_filter_levels)

internal fun Context.modeLabels(): Array<String> = resources.getStringArray(R.array.match_filter_modes)

internal fun Context.dayLabels(): Array<String> = resources.getStringArray(R.array.match_filter_days)

internal fun Context.levelLabel(level: MatchLevel): String = levelLabels()[level.ordinal]

internal fun Context.modeLabel(mode: StudyMode): String = modeLabels()[mode.ordinal]

internal fun Context.timeSlotLabel(slot: WeeklyTimeSlot): String = getString(
    R.string.match_filter_slot,
    dayLabels()[slot.day.ordinal],
    formatTime(slot.startMinute),
    formatTime(slot.endMinute)
)

private fun formatTime(minute: Int): String =
    String.format(Locale.ROOT, "%02d:%02d", minute / 60, minute % 60)
