package com.ute.co_study.feature.learner.model

internal enum class StudyConversation(val title: String, val initials: String, val subtitle: String) {
    MINH_ANH("Minh Anh", "MA", "Bạn học · Kotlin / Android"),
    ANDROID("Nhóm Android K26", "A", "Nhóm học Android"),
    HOANG_NAM("Hoàng Nam", "HN", "Bạn học Co-Study");

    val initialMessages: List<StudyMessage>
        get() = when (this) {
            MINH_ANH -> listOf(
                StudyMessage("Chào bạn! Mình có thể hỗ trợ phần Activity và Intent trong Kotlin.", false),
                StudyMessage("Tuyệt! Mình muốn thực hành truyền dữ liệu giữa các màn hình.", true),
                StudyMessage("Mình cùng ôn Kotlin nhé? Bạn chọn lịch phù hợp nha.", false)
            )
            ANDROID -> listOf(StudyMessage("Hoàng: Mình vừa chia sẻ tài liệu mới.", false))
            HOANG_NAM -> listOf(StudyMessage("Cảm ơn bạn, hẹn buổi học sau!", false))
        }
}

internal data class StudyMessage(val text: String, val sentByLearner: Boolean)
