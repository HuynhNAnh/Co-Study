package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudyStatus

class ScheduleActivity : StudyActivity(R.layout.activity_schedule) {
    override val refreshOnReturn = true

    override fun render() {
        val sessions = study.sessions().asReversed()
        val upcoming = sessions.filter { it.status !in setOf(StudyStatus.COMPLETED, StudyStatus.CANCELED) }
        status("${upcoming.size} buổi học hoặc đề xuất đang mở")
        val featured = upcoming.firstOrNull()
        if (featured != null) {
            val month = featured.slot.date.substring(3)
            val days = upcoming.filter { it.slot.date.endsWith(month) }.map { it.slot.date.take(2) }
            card("THÁNG ${month.replace("/", " / ")}", days.distinct().sorted().joinToString("  ·  "), Tone.MINT)
            sessionCard(featured)
            button("Xem chi tiết buổi học", parent = body) { openDetails(featured) }
        } else {
            note("Bạn chưa có buổi học sắp tới. Mở Tin nhắn để đề xuất lịch học.")
        }
        val others = sessions.filter { it.id != featured?.id }
        if (others.isNotEmpty()) label("Các buổi học khác")
        others.forEach { item ->
            row(item.subject, "${item.status.label} · ${item.slot.display}") { openDetails(item) }
        }
        learnerTabs(3)
    }
}
