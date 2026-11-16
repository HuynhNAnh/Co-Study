package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudyStatus

class StudyDetailsActivity : StudyActivity(R.layout.activity_study_details) {
    override val refreshOnReturn = true

    override fun render() {
        val item = expect(StudyStatus.CONFIRMED) ?: return
        partnerCard(item.partner)
        sessionCard(item)
        note("Chuẩn bị tài liệu và trao đổi nội dung cần học với bạn học trước buổi học.")
        button("Đổi lịch", Tone.PLAIN) { openSession(RescheduleStudyActivity::class.java, item.id) }
        button("Hủy lịch", Tone.DANGER) { openSession(CancelStudyActivity::class.java, item.id) }
        button("Đánh dấu hoàn thành") { changed(study.complete(item.id), CompletedStudyActivity::class.java) }
    }
}
