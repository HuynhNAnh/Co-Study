package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus

class ModerationNotificationsActivity : ModerationActivity(R.layout.activity_moderation_case_notifications) {
    override val refreshOnReturn = true

    override fun render() {
        val items = reports.queue().filter { it.status != ReportStatus.DISMISSED }
        items.forEach { item ->
            row(item.lastEvent?.action?.label ?: "Có báo cáo mới", "${item.id} · ${item.status.label}\n${item.title}") { showReport(item) }
        }
        if (items.isEmpty()) note("Chưa có thông báo mới.")
        button("Về tổng quan", Tone.PLAIN) { returnTo(ModerationDashboardActivity::class.java) }
    }
}
