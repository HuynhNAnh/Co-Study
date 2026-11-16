package com.ute.co_study.feature.moderation.ui.report

import com.ute.co_study.databinding.ActivityReportDismissedBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.feature.moderation.ui.overview.ModerationOverviewActivity
import com.ute.co_study.shared.model.ReportStatus

class ReportDismissedActivity : ModerationActivity<ActivityReportDismissedBinding>(ActivityReportDismissedBinding::inflate) {
    override fun bind() {
        binding.backToListButton.setOnClickListener {
            returnTo(ReportListActivity::class.java)
            finish()
        }
        binding.backToOverviewButton.setOnClickListener {
            returnTo(ModerationOverviewActivity::class.java)
            finish()
        }
    }

    override fun refresh() {
        val item = currentReport(ReportStatus.DISMISSED) ?: return
        describe(item)
        binding.resultDetail.text = item.lastEvent?.reason.orEmpty()
    }
}
