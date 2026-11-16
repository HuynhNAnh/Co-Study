package com.ute.co_study.feature.learner.ui.progress

import com.ute.co_study.R
import com.ute.co_study.shared.model.LocalStudyDay
import com.ute.co_study.feature.learner.ui.study.StudyActivity

class StreakHistoryActivity : StudyActivity(R.layout.activity_streak_history) {
    override val refreshOnReturn = true

    override fun render() {
        val completed = study.progress().completed
        if (completed.isEmpty()) note("Các buổi học đã hoàn thành sẽ xuất hiện tại đây.")
        val now = System.currentTimeMillis()
        completed.groupBy { LocalStudyDay.format(it.completedAt) }.forEach { (date, items) ->
            label(date)
            items.forEach { item ->
                row(item.subject, "${item.partner} · ${item.recognitionStatus(now)}") {
                    openSession(StudyRecognitionActivity::class.java, item.id)
                }
            }
        }
    }
}
