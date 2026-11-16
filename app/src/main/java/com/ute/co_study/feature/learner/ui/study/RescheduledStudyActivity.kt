package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudyStatus

class RescheduledStudyActivity : StudyActivity(R.layout.activity_rescheduled_study) {
    override val refreshOnReturn = true

    override fun render() {
        val item = expect(StudyStatus.RESCHEDULED) ?: return
        partnerCard(item.partner)
        sessionCard(item)
        note("${item.partner} đã đồng ý đổi lịch. Lịch mới là ngày ${item.slot.date}.")
        button("Đánh dấu hoàn thành") { changed(study.complete(item.id), CompletedRescheduleActivity::class.java) }
        button("Đổi lịch", Tone.PLAIN) { openSession(RescheduleStudyActivity::class.java, item.id) }
        button("Hủy lịch", Tone.DANGER) { openSession(CancelStudyActivity::class.java, item.id) }
    }
}
