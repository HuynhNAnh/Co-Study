package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudyStatus

class ScheduleActivity : StudyActivity(R.layout.activity_schedule) {
    override val refreshOnReturn = true

    override fun render() {
        val sessions = study.sessions()
        val now = System.currentTimeMillis()
        val openSessions = sessions.filter { it.status !in closedStatuses }
            .map { it to it.slot.startsAt }
            .sortedWith(compareBy({ it.second < now }, { it.second }))
            .map { it.first }
        status("${openSessions.size} buổi học hoặc đề xuất đang mở")

        val featured = openSessions.firstOrNull {
            it.status in confirmedStatuses && it.slot.startsAt >= now
        } ?: openSessions.firstOrNull()
        if (featured == null) {
            note("Bạn chưa có buổi học đang mở. Mở Tin nhắn để đề xuất lịch học.")
        } else {
            val month = featured.slot.date.substring(3)
            val days = openSessions.filter { it.slot.date.endsWith(month) }.map { it.slot.date.take(2) }
            card("THÁNG ${month.replace("/", " / ")}", days.distinct().sorted().joinToString("  ·  "), Tone.MINT)
            label("Buổi học với ${featured.partner}")
            sessionCard(featured)
            button("Xem chi tiết buổi học", parent = body) { openDetails(featured) }
        }

        if (openSessions.size > 1) label("Lịch học và đề xuất khác")
        openSessions.filter { it.id != featured?.id }.forEach { item ->
            row(item.subject, "${item.partner} · ${item.status.label}\n${item.slot.display}") { openDetails(item) }
        }

        val history = sessions.filter { it.status in closedStatuses }.sortedByDescending { it.slot.startsAt }
        if (history.isNotEmpty()) label("Lịch sử buổi học")
        history.forEach { item ->
            row(item.subject, "${item.partner} · ${item.status.label}\n${item.slot.display}") { openDetails(item) }
        }

        learnerTabs(3)
    }

    private companion object {
        val closedStatuses = setOf(StudyStatus.COMPLETED, StudyStatus.CANCELED)
        val confirmedStatuses = setOf(StudyStatus.CONFIRMED, StudyStatus.RESCHEDULED)
    }
}
