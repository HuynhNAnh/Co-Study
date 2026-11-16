package com.ute.co_study.feature.moderation.ui.overview

import com.ute.co_study.databinding.ActivityModerationOverviewBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.R
import androidx.core.view.isVisible
import com.ute.co_study.feature.moderation.ui.history.ModerationNotificationsActivity
import com.ute.co_study.feature.moderation.ui.report.ReportListActivity
import com.ute.co_study.feature.moderation.ui.setupModerationNavigation
import com.ute.co_study.feature.moderation.ui.showReportCards
import com.ute.co_study.shared.model.ReportStatus

class ModerationOverviewActivity : ModerationActivity<ActivityModerationOverviewBinding>(ActivityModerationOverviewBinding::inflate) {
    override fun bind() {
        setupModerationNavigation(this, binding.moderationNavigation)
        binding.notificationsBanner.setOnClickListener { open(ModerationNotificationsActivity::class.java) }
        binding.seeAllButton.setOnClickListener { returnTo(ReportListActivity::class.java) }
    }

    override fun refresh() {
        val items = reports.queue()
        val active = items.filter { it.status in activeStatuses }
        binding.newCount.text = items.count { it.status == ReportStatus.NEW }.toString()
        binding.processingCount.text = items.count { it.status == ReportStatus.IN_REVIEW }.toString()
        binding.escalatedCount.text = items.count { it.status == ReportStatus.ESCALATED }.toString()
        binding.emptyReports.isVisible = active.isEmpty()
        binding.reportCards.showReportCards(active.take(3), { it.title }, {
            getString(R.string.mod_report_people, it.sender, it.subject)
        }, ::showReport)
    }

    private companion object {
        val activeStatuses = setOf(ReportStatus.NEW, ReportStatus.IN_REVIEW, ReportStatus.RETURNED)
    }
}
