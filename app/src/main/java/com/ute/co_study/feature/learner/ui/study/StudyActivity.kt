package com.ute.co_study.feature.learner.ui.study

import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudyConversation
import com.ute.co_study.feature.learner.model.StudySession
import com.ute.co_study.feature.learner.model.StudySlot
import com.ute.co_study.feature.learner.model.StudyStatus
import com.ute.co_study.feature.learner.ui.LearnerActivity
import com.ute.co_study.feature.learner.viewmodel.study.LearnerStudyViewModel

abstract class StudyActivity(layout: Int) : LearnerActivity(layout) {
    private val model: LearnerStudyViewModel by viewModels()
    internal val study get() = model.study

    internal fun studySession(): StudySession? {
        return study.find(intent.getLongExtra(SESSION_ID, -1L)) ?: run {
            message("Không tìm thấy buổi học")
            finish()
            null
        }
    }

    internal fun expect(vararg states: StudyStatus): StudySession? {
        val item = studySession() ?: return null
        if (item.status in states) return item
        finish()
        return null
    }

    internal fun completedActions(item: StudySession) {
        if (item.partnerConfirmedAt == 0L && System.currentTimeMillis() - item.completedAt <= 48L * 60 * 60 * 1000) {
            button("Mô phỏng bạn học xác nhận tham gia", Tone.PLAIN, body) {
                if (study.confirmAttendance(item.id) != null) refresh()
            }
        } else if (item.partnerConfirmedAt != 0L) {
            note("Hai bạn đã xác nhận tham gia.")
        }
        if (item.stars == 0) {
            button("Đánh giá bạn học") { openSession(ReviewPartnerActivity::class.java, item.id) }
        } else {
            button("Xem đánh giá đã gửi") { openSession(ReviewSentActivity::class.java, item.id) }
        }
        button("Về tin nhắn", Tone.PLAIN) { returnTo(MessagesActivity::class.java) }
    }

    internal fun openSession(target: Class<out AppCompatActivity>, id: Long) =
        open(target, Bundle().apply { putLong(SESSION_ID, id) })

    internal fun openDetails(item: StudySession) {
        val target = when (item.status) {
            StudyStatus.PENDING -> PendingStudyActivity::class.java
            StudyStatus.CONFIRMED -> StudyDetailsActivity::class.java
            StudyStatus.RESCHEDULING -> PendingRescheduleActivity::class.java
            StudyStatus.RESCHEDULED -> RescheduledStudyActivity::class.java
            StudyStatus.COMPLETED -> if (item.reason.isEmpty()) CompletedStudyActivity::class.java else CompletedRescheduleActivity::class.java
            StudyStatus.CANCELED -> CanceledStudyActivity::class.java
        }
        openSession(target, item.id)
    }

    internal fun conversation(): StudyConversation =
        StudyConversation.entries.find { it.name == intent.getStringExtra(CONVERSATION) }
            ?: StudyConversation.MINH_ANH

    internal fun openConversation(conversation: StudyConversation) =
        open(ConversationActivity::class.java, Bundle().apply { putString(CONVERSATION, conversation.name) })

    internal fun openConversationFor(item: StudySession) =
        openConversation(StudyConversation.entries.firstOrNull { it.title == item.partner } ?: StudyConversation.MINH_ANH)

    internal fun sessionCard(item: StudySession) {
        card(item.subject, "${item.status.label}\n\nNGÀY HỌC\n${item.slot.date}\n\nTHỜI GIAN\n${item.slot.start} – ${item.slot.end} · ${item.slot.minutes} phút\n\nHÌNH THỨC\n${item.mode}")
    }

    internal fun partnerCard(name: String) {
        val partner = StudyConversation.entries.firstOrNull { it.title == name } ?: StudyConversation.MINH_ANH
        person(partner.initials, partner.title, partner.subtitle)
    }

    internal fun slotFields(slot: StudySlot): SlotFields = SlotFields(
        input("Ngày học", slot.date, "DD/MM/YYYY", maxLength = 10),
        input("Giờ bắt đầu", slot.start, "HH:MM", maxLength = 5),
        input("Giờ kết thúc", slot.end, "HH:MM", maxLength = 5)
    )

    internal fun readSlot(fields: SlotFields): StudySlot? {
        val slot = StudySlot.parse(
            fields.date.text.toString().trim(), fields.start.text.toString().trim(), fields.end.text.toString().trim()
        )
        if (slot == null) {
            fields.end.error = "Nhập ngày DD/MM/YYYY và giờ HH:MM. Giờ kết thúc phải sau giờ bắt đầu."
            fields.end.requestFocus()
        }
        return slot
    }

    internal fun changed(item: StudySession?, target: Class<out AppCompatActivity>) {
        if (item == null) {
            message("Trạng thái buổi học đã thay đổi")
            refresh()
        } else {
            openSession(target, item.id)
            finish()
        }
    }

    internal data class SlotFields(val date: EditText, val start: EditText, val end: EditText)

    internal companion object {
        const val SESSION_ID = "study_session_id"
        const val CONVERSATION = "study_conversation"
    }
}
