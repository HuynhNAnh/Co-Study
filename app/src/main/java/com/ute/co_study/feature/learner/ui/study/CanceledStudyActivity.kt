package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudyStatus

class CanceledStudyActivity : StudyActivity(R.layout.activity_canceled_study) {
    override fun render() {
        val item = expect(StudyStatus.CANCELED) ?: return
        result("Buổi học đã được hủy", "${item.subject}\n${item.slot.display}", negative = true)
        info("Lý do đã gửi cho ${item.partner}", item.reason)
        note("Khi sẵn sàng, hãy trao đổi để đề xuất một buổi học mới.")
        button("Nhắn tin cho ${item.partner}") { openConversationFor(item); finish() }
        button("Về danh sách tin nhắn", Tone.PLAIN) { returnTo(MessagesActivity::class.java) }
    }
}
