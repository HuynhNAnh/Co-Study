package com.ute.co_study.feature.moderation.ui.history

import com.ute.co_study.databinding.ActivityModerationHistoryBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import androidx.core.view.isVisible
import com.ute.co_study.feature.moderation.ui.setupModerationNavigation
import com.ute.co_study.feature.moderation.ui.showReportCards
import com.ute.co_study.shared.model.ReportAction

class ModerationHistoryActivity : ModerationActivity<ActivityModerationHistoryBinding>(ActivityModerationHistoryBinding::inflate) {
    override fun bind() {
        setupModerationNavigation(this, binding.moderationNavigation)
    }

    override fun refresh() {
        val items = reports.queue().filter { report -> report.history.any { it.action != ReportAction.CLAIM } }
        binding.emptyReports.isVisible = items.isEmpty()
        binding.reportCards.showReportCards(items, { it.lastEvent?.action?.label ?: it.title }, { item ->
            val event = item.lastEvent
            if (event == null) item.subject else "${item.subject}\n${event.at} · ${event.actor}"
        }) { openReport(HistoryDetailActivity::class.java, it) }
    }
}
