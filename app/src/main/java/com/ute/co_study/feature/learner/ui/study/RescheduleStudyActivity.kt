package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudyStatus

class RescheduleStudyActivity : StudyActivity(R.layout.activity_reschedule_study) {
    override fun render() {
        val item = expect(StudyStatus.CONFIRMED, StudyStatus.RESCHEDULED) ?: return
        info("Lịch hiện tại", item.slot.display)
        val fields = slotFields(item.slot)
        val reason = input("Lý do đổi lịch", hint = "Giải thích với bạn học", multiline = true)
        note("Lịch hiện tại vẫn có hiệu lực đến khi bạn học đồng ý với đề nghị mới.")
        button("Gửi đề nghị đổi lịch") {
            val updated = readSlot(fields) ?: return@button
            if (updated == item.slot) {
                fields.date.error = "Hãy chọn thời gian khác lịch hiện tại"
                fields.date.requestFocus()
                return@button
            }
            val text = required(reason) ?: return@button
            changed(study.reschedule(item.id, updated, text), PendingRescheduleActivity::class.java)
        }
        button("Giữ lịch hiện tại", Tone.PLAIN) { finish() }
    }
}
