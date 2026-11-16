package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudyStatus

class CompletedRescheduleActivity : StudyActivity(R.layout.activity_completed_reschedule) {
    override val refreshOnReturn = true

    override fun render() {
        val item = expect(StudyStatus.COMPLETED) ?: return
        result("Bạn đã học thêm điều mới!", "Buổi học theo lịch mới với ${item.partner} đã hoàn thành.")
        sessionCard(item)
        completedActions(item)
    }
}
