package com.ute.co_study.feature.moderation.ui.report

import com.ute.co_study.databinding.ActivityReportListBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.R
import androidx.core.view.isVisible
import com.ute.co_study.feature.moderation.ui.setupModerationNavigation
import com.ute.co_study.feature.moderation.ui.showReportCards

class ReportListActivity : ModerationActivity<ActivityReportListBinding>(ActivityReportListBinding::inflate) {
    override fun bind() {
        setupModerationNavigation(this, binding.moderationNavigation)
        binding.searchFilterCard.setOnClickListener { open(ReportFilterActivity::class.java) }
    }

    override fun refresh() {
        val items = moderation.filtered(session.name)
        binding.screenSubtitle.text = resources.getQuantityString(R.plurals.mod_report_count, items.size, items.size)
        binding.emptyReports.isVisible = items.isEmpty()
        binding.reportCards.showReportCards(items, { it.title }, {
            getString(R.string.mod_report_people, it.sender, it.subject)
        }, ::showReport)
    }
}
