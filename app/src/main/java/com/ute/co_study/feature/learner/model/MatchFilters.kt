package com.ute.co_study.feature.learner.model

data class MatchFilters(
    val skill: String,
    val level: MatchLevel,
    val mode: StudyMode,
    val timeSlot: WeeklyTimeSlot?
)

enum class MatchLevel { ANY, BEGINNER_TO_INTERMEDIATE, BEGINNER, INTERMEDIATE, ADVANCED }

enum class StudyMode { ANY, ONLINE, IN_PERSON }

enum class StudyDay { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }

data class WeeklyTimeSlot(
    val day: StudyDay,
    val startMinute: Int,
    val endMinute: Int
) {
    init {
        require(startMinute in 0..1438 && endMinute in (startMinute + 1)..1439)
    }

    fun overlaps(other: WeeklyTimeSlot): Boolean =
        day == other.day && startMinute < other.endMinute && other.startMinute < endMinute
}
