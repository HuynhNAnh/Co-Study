package com.ute.co_study.feature.learner.model

internal data class SamplePartner(
    val conversation: StudyConversation,
    val bio: String,
    val shares: String,
    val wants: String,
    val level: MatchLevel,
    val modes: Set<StudyMode>,
    val availability: List<WeeklyTimeSlot>,
    val matchScore: Int,
    val trustScore: Int,
    val rating: String,
    val completedSessions: Int
)

internal object SamplePartners {
    private val online = setOf(StudyMode.ONLINE)
    private val inPerson = setOf(StudyMode.IN_PERSON)
    private val flexible = online + inPerson

    val all = listOf(
        SamplePartner(
            StudyConversation.MINH_ANH, "Sinh viên CNTT · Đà Nẵng", "Kotlin / Android", "Figma và thiết kế giao diện",
            MatchLevel.BEGINNER_TO_INTERMEDIATE, online, listOf(slot(StudyDay.THURSDAY, 19, 20)), 92, 96, "4,9/5", 18
        ),
        SamplePartner(
            StudyConversation.DUC_HUY, "Sinh viên Kỹ thuật phần mềm · Đà Nẵng", "Kotlin / Android, Jetpack Compose", "SQL",
            MatchLevel.INTERMEDIATE, flexible, listOf(slot(StudyDay.THURSDAY, 19, 21), slot(StudyDay.SATURDAY, 9, 11)),
            90, 94, "4,8/5", 14
        ),
        SamplePartner(
            StudyConversation.BAO_NGOC, "Sinh viên Thiết kế · Huế", "Figma, Auto Layout và UI", "Kotlin / Android",
            MatchLevel.INTERMEDIATE, online, listOf(slot(StudyDay.WEDNESDAY, 20, 21), slot(StudyDay.SATURDAY, 19, 21)),
            88, 93, "4,8/5", 16
        ),
        SamplePartner(
            StudyConversation.HOANG_NAM, "Sinh viên Hệ thống thông tin · Đà Nẵng", "SQL, thiết kế cơ sở dữ liệu", "Python",
            MatchLevel.INTERMEDIATE, flexible, listOf(slot(StudyDay.TUESDAY, 19, 21), slot(StudyDay.FRIDAY, 19, 20)),
            87, 95, "4,9/5", 21
        ),
        SamplePartner(
            StudyConversation.THU_HA, "Sinh viên Khoa học dữ liệu · Huế", "Python, phân tích dữ liệu", "Tiếng Anh giao tiếp",
            MatchLevel.ADVANCED, online, listOf(slot(StudyDay.MONDAY, 20, 21), slot(StudyDay.SUNDAY, 14, 16)),
            85, 97, "4,9/5", 24
        ),
        SamplePartner(
            StudyConversation.PHUONG_LINH, "Sinh viên Ngôn ngữ Anh · Đà Nẵng", "Tiếng Anh giao tiếp và thuyết trình", "Figma",
            MatchLevel.BEGINNER, flexible, listOf(slot(StudyDay.WEDNESDAY, 19, 20), slot(StudyDay.SUNDAY, 9, 11)),
            84, 92, "4,7/5", 12
        ),
        SamplePartner(
            StudyConversation.HAI_YEN, "Sinh viên CNTT · Đà Nẵng", "Git, Kotlin / Android cơ bản", "Jetpack Compose",
            MatchLevel.BEGINNER, online, listOf(slot(StudyDay.THURSDAY, 19, 21), slot(StudyDay.SUNDAY, 15, 17)),
            82, 91, "4,7/5", 9
        ),
        SamplePartner(
            StudyConversation.QUANG_MINH, "Sinh viên Khoa học máy tính · Quảng Nam", "C++, thuật toán, quy hoạch động", "Git",
            MatchLevel.ADVANCED, inPerson, listOf(slot(StudyDay.SATURDAY, 14, 16)), 80, 94, "4,8/5", 20
        )
    )

    fun find(conversation: StudyConversation): SamplePartner? =
        all.firstOrNull { it.conversation == conversation }

    private fun slot(day: StudyDay, startHour: Int, endHour: Int) =
        WeeklyTimeSlot(day, startHour * 60, endHour * 60)
}
