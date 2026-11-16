package com.ute.co_study.shared.model

internal data class StudyRecognitionPolicy(
    val minimumMinutes: Int = 25,
    val confirmationHours: Int = 48,
    val streakThresholds: List<Int> = listOf(3, 7, 30),
    val partnerThreshold: Int = 5
) {
    val confirmationMillis get() = confirmationHours * 3_600_000L

    companion object {
        fun from(rules: CommunityRules) = StudyRecognitionPolicy(
            rules.studyMinutes, rules.studyConfirmHours, rules.badgeDays, rules.partnerSessions
        )
    }
}
