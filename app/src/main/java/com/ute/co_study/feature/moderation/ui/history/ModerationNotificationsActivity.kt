package com.ute.co_study.feature.moderation.ui.history

import com.ute.co_study.databinding.ActivityModerationNotificationsBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.R
import androidx.core.view.isVisible
import com.ute.co_study.databinding.ItemModerationNotificationBinding
import com.ute.co_study.shared.model.ReportStatus

class ModerationNotificationsActivity : ModerationActivity<ActivityModerationNotificationsBinding>(ActivityModerationNotificationsBinding::inflate) {
    override fun bind() {
        binding.backToOverviewButton.setOnClickListener { finish() }
    }

    override fun refresh() {
        val items = reports.queue().filter { it.status == ReportStatus.NEW || it.history.isNotEmpty() }
        binding.emptyReports.isVisible = items.isEmpty()
        binding.notificationCards.removeAllViews()
        items.forEach { report ->
            val view = ItemModerationNotificationBinding.inflate(layoutInflater, binding.notificationCards, false)
            val event = report.lastEvent
            view.notificationTitle.text = event?.action?.label ?: getString(R.string.mod_new_report_notice)
            view.notificationSummary.text = "${report.id} · ${report.status.label}\n${report.title}" +
                event?.let { "\n${it.at}" }.orEmpty()
            view.root.setOnClickListener { showReport(report) }
            binding.notificationCards.addView(view.root)
        }
    }
}
