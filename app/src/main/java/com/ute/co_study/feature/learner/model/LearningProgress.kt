package com.ute.co_study.feature.learner.model

import com.ute.co_study.shared.model.LocalStudyDay
import com.ute.co_study.shared.model.StudyRecognitionPolicy

internal data class LearningBadge(
    val kind: BadgeKind,
    val count: Int,
    val earnedAt: Long = 0,
    val partner: String = ""
) {
    val key get() = "${kind.name}:$count"
    val earned get() = earnedAt > 0
    val title get() = if (kind == BadgeKind.STREAK) "Chuỗi $count ngày" else "Học cùng bạn · $count buổi"
    val requirement
        get() = if (kind == BadgeKind.STREAK) "Ghi nhận buổi học hợp lệ trong $count ngày liên tiếp."
            else "Hoàn thành $count buổi học hợp lệ cùng một bạn học."
}

internal enum class BadgeKind { STREAK, PARTNER }

internal class LearningProgress(
    val completed: List<StudySession>,
    policy: StudyRecognitionPolicy,
    now: Long
) {
    private val eligible = completed.filter { it.recognized }
    private val days = eligible.map { LocalStudyDay.index(it.completedAt) }.toSet()
    val validDays get() = days.size
    val current: Int = currentStreak(LocalStudyDay.index(now), days)
    val longest: Int = longestStreak(days)
    val badges: List<LearningBadge> = buildBadges(policy)

    private fun buildBadges(policy: StudyRecognitionPolicy): List<LearningBadge> {
        val awarded = linkedMapOf<String, LearningBadge>()
        val seenDays = mutableSetOf<Long>()
        val partnerCounts = mutableMapOf<String, Int>()
        eligible.sortedBy { it.partnerConfirmedAt }.forEach { item ->
            seenDays.add(LocalStudyDay.index(item.completedAt))
            val longestAtConfirmation = longestStreak(seenDays)
            val count = (partnerCounts[item.partner] ?: 0) + 1
            partnerCounts[item.partner] = count
            item.policy.streakThresholds.filter { longestAtConfirmation >= it }.forEach { threshold ->
                val badge = LearningBadge(BadgeKind.STREAK, threshold, item.partnerConfirmedAt)
                if (badge.key !in awarded) awarded[badge.key] = badge
            }
            if (count >= item.policy.partnerThreshold) {
                val badge = LearningBadge(BadgeKind.PARTNER, item.policy.partnerThreshold, item.partnerConfirmedAt, item.partner)
                if (badge.key !in awarded) awarded[badge.key] = badge
            }
        }
        val configured = policy.streakThresholds.map { LearningBadge(BadgeKind.STREAK, it) } +
            LearningBadge(BadgeKind.PARTNER, policy.partnerThreshold)
        return (configured.map { awarded[it.key] ?: it } + awarded.values).distinctBy { it.key }
    }

    private fun currentStreak(today: Long, days: Set<Long>): Int {
        var end = if (today in days) today else today - 1
        var length = 0
        while (end in days) {
            end--
            length++
        }
        return length
    }

    private fun longestStreak(days: Set<Long>): Int {
        var longest = 0
        var streak = 0
        var last = Long.MIN_VALUE
        days.sorted().forEach { day ->
            streak = if (day == last + 1) streak + 1 else 1
            longest = maxOf(longest, streak)
            last = day
        }
        return longest
    }
}
