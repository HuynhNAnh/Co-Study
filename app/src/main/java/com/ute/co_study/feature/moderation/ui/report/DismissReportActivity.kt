package com.ute.co_study.feature.moderation.ui.report

import com.ute.co_study.databinding.ActivityDismissReportBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.shared.model.ReportStatus
import com.ute.co_study.shared.model.ReportAction

class DismissReportActivity : ModerationActivity<ActivityDismissReportBinding>(ActivityDismissReportBinding::inflate) {
    override fun bind() {
        val item = currentReport(ReportStatus.IN_REVIEW) ?: return
        describe(item)
        binding.backReviewButton.setOnClickListener { finish() }
        binding.confirmDismissButton.setOnClickListener {
            val reason = required(binding.dismissReasonInput) ?: return@setOnClickListener
            updateReport(item, ReportAction.DISMISS, reason, ReportDismissedActivity::class.java)
        }
    }
}
