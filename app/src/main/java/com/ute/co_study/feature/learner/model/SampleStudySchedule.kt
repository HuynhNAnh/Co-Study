package com.ute.co_study.feature.learner.model

import com.ute.co_study.shared.model.LocalStudyDay

internal object SampleStudySchedule {
    private const val ONLINE = "Trực tuyến · Google Meet"
    private const val IN_PERSON = "Gặp trực tiếp · Thư viện"

    private val examples = listOf(
        Example(1L, StudyConversation.MINH_ANH, 1, "19:00", "20:00", ONLINE, StudyStatus.CONFIRMED),
        Example(-101L, StudyConversation.DUC_HUY, 2, "20:00", "21:00", ONLINE, StudyStatus.PENDING),
        Example(-102L, StudyConversation.HOANG_NAM, 3, "18:30", "19:30", IN_PERSON, StudyStatus.CONFIRMED),
        Example(
            -103L, StudyConversation.BAO_NGOC, 4, "19:00", "20:00", ONLINE, StudyStatus.RESCHEDULING,
            newDay = 6, reason = "Bảo Ngọc bận nộp bài thiết kế và đề nghị dời buổi học."
        ),
        Example(
            -104L, StudyConversation.PHUONG_LINH, 5, "09:00", "09:45", IN_PERSON, StudyStatus.RESCHEDULED,
            reason = "Hai bạn đã thống nhất chuyển sang buổi sáng để luyện thuyết trình."
        ),
        Example(-105L, StudyConversation.THU_HA, 7, "20:00", "21:15", ONLINE, StudyStatus.CONFIRMED),
        Example(
            -106L, StudyConversation.HAI_YEN, -2, "19:30", "20:30", ONLINE, StudyStatus.CANCELED,
            reason = "Hải Yến bận lịch làm đồ án và sẽ đề xuất giờ khác."
        ),
        Example(-107L, StudyConversation.QUANG_MINH, 8, "14:00", "15:30", IN_PERSON, StudyStatus.PENDING)
    )

    fun missingFrom(ids: Set<Long>, now: Long): List<StudySession> =
        examples.filterNot { it.id in ids }.map { example ->
            StudySession(
                id = example.id,
                partner = example.conversation.title,
                subject = checkNotNull(example.conversation.studyTopic),
                slot = example.slot(now, example.day),
                mode = example.mode,
                status = example.status,
                newSlot = example.newDay?.let { example.slot(now, it) },
                reason = example.reason
            )
        }

    private data class Example(
        val id: Long,
        val conversation: StudyConversation,
        val day: Int,
        val start: String,
        val end: String,
        val mode: String,
        val status: StudyStatus,
        val newDay: Int? = null,
        val reason: String = ""
    ) {
        fun slot(now: Long, day: Int) = StudySlot(LocalStudyDay.format(now, daysAfter = day), start, end)
    }
}
