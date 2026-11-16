package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudyStatus

class CancelStudyActivity : StudyActivity(R.layout.activity_cancel_study) {
    override fun render() {
        val item = expect(StudyStatus.CONFIRMED, StudyStatus.RESCHEDULED, StudyStatus.RESCHEDULING) ?: return
        partnerCard(item.partner)
        info("Buổi học sẽ hủy", "${item.subject}\n${item.slot.display}")
        val reason = input("Lý do hủy", hint = "Nhập lý do hủy lịch", multiline = true)
        note("Sau khi xác nhận, buổi học sẽ bị hủy và bạn học nhận được lý do của bạn.", Tone.DANGER)
        button("Xác nhận hủy lịch", Tone.DANGER) {
            val explanation = required(reason) ?: return@button
            changed(study.cancel(item.id, explanation), CanceledStudyActivity::class.java)
        }
        button("Giữ lịch học", Tone.PLAIN) { finish() }
    }
}
