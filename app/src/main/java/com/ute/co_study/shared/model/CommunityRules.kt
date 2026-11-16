package com.ute.co_study.shared.model

internal data class CommunityRules(
    val dailyInvites: Int = 5,
    val reportHours: Int = 48,
    val studyMinutes: Int = 25,
    val studyConfirmHours: Int = 48,
    val badgeDays: List<Int> = listOf(3, 7, 30),
    val partnerSessions: Int = 5
) {
    val valid: Boolean
        get() = dailyInvites in 1..100 && reportHours in 1..720 && studyMinutes in 1..240 &&
            studyConfirmHours in 1..720 && badgeDays.size == 3 && badgeDays.distinct() == badgeDays &&
            badgeDays == badgeDays.sorted() && badgeDays.all { it in 1..365 } && partnerSessions in 1..100
}
