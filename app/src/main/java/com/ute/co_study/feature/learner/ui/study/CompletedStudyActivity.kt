package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudyStatus

class CompletedStudyActivity : StudyActivity(R.layout.activity_completed_study) {
    override val refreshOnReturn = true

    override fun render() {
        val item = expect(StudyStatus.COMPLETED) ?: return
        result("Bạn đã học thêm điều mới!", "Buổi học với ${item.partner} đã hoàn thành. Hãy chia sẻ trải nghiệm của bạn.")
        sessionCard(item)
        completedActions(item)
    }
}
