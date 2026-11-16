package com.ute.co_study.feature.learner.model

import com.ute.co_study.shared.model.StudyRecognitionPolicy
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

internal enum class StudyStatus(val label: String) {
    PENDING("Chờ xác nhận"),
    CONFIRMED("Đã xác nhận"),
    RESCHEDULING("Chờ đổi lịch"),
    RESCHEDULED("Đã xác nhận lịch mới"),
    COMPLETED("Đã hoàn thành"),
    CANCELED("Đã hủy")
}

internal data class StudySession(
    val id: Long,
    val partner: String,
    val subject: String,
    val slot: StudySlot,
    val mode: String,
    val status: StudyStatus,
    val newSlot: StudySlot? = null,
    val reason: String = "",
    val stars: Int = 0,
    val review: String = "",
    val completedAt: Long = 0L,
    val partnerConfirmedAt: Long = 0L,
    val policy: StudyRecognitionPolicy = StudyRecognitionPolicy()
) {
    val recognized: Boolean
        get() = status == StudyStatus.COMPLETED && completedAt > 0 &&
            slot.minutes >= policy.minimumMinutes &&
            partnerConfirmedAt - completedAt in 0L..policy.confirmationMillis

    fun canConfirm(now: Long): Boolean = status == StudyStatus.COMPLETED && completedAt > 0 &&
        partnerConfirmedAt == 0L && now - completedAt in 0L..policy.confirmationMillis

    fun recognitionStatus(now: Long): String = when {
        slot.minutes < policy.minimumMinutes -> "Chưa đủ ${policy.minimumMinutes} phút học"
        recognized -> "Đã ghi nhận"
        canConfirm(now) -> "Chờ bạn học xác nhận"
        else -> "Đã hết hạn xác nhận"
    }
}

internal data class StudySlot(val date: String, val start: String, val end: String) {
    val display: String get() = "$date · $start – $end"
    val minutes: Int get() = toMinutes(end) - toMinutes(start)

    companion object {
        private val datePattern = Regex("""\d{2}/\d{2}/\d{4}""")
        private val timePattern = Regex("""([01]\d|2[0-3]):[0-5]\d""")

        fun parse(date: String, start: String, end: String): StudySlot? {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.US).apply {
                isLenient = false
                timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh")
            }
            if (!datePattern.matches(date) || !timePattern.matches(start) || !timePattern.matches(end)) return null
            try {
                format.parse(date)
            } catch (_: java.text.ParseException) {
                return null
            }
            return StudySlot(date, start, end).takeIf { it.minutes > 0 }
        }

        private fun toMinutes(time: String): Int =
            time.substring(0, 2).toInt() * 60 + time.substring(3).toInt()
    }
}
