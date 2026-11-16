package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportAction

class ModerationHistoryActivity : ModerationActivity(R.layout.activity_moderation_history) {
    override val refreshOnReturn = true

    override fun render() {
        val history = reports.queue().filter { item -> item.history.any { it.action != ReportAction.CLAIM } }
        history.forEach { item ->
            row(item.lastEvent?.action?.label ?: item.title, "${item.id} · ${item.status.label}\n${item.lastEvent?.at.orEmpty()}") {
                openReport(ModerationHistoryDetailActivity::class.java, item)
            }
        }
        if (history.isEmpty()) note("Chưa có quyết định xử lý nào.")
        moderationTabs(2)
    }
}
