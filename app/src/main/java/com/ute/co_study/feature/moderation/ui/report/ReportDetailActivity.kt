package com.ute.co_study.feature.moderation.ui.report

import com.ute.co_study.databinding.ActivityReportDetailBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.R
import androidx.core.view.isVisible
import com.ute.co_study.feature.moderation.ui.subjectInitials
import com.ute.co_study.shared.model.ReportStatus
import com.ute.co_study.shared.model.ReportAction

class ReportDetailActivity : ModerationActivity<ActivityReportDetailBinding>(ActivityReportDetailBinding::inflate) {
    override fun bind() = Unit

    override fun refresh() {
        val item = currentReport() ?: return
        describe(item)
        binding.reportSender.text = listOf(item.sender, item.createdAt).filter(String::isNotBlank).joinToString(" · ")
        binding.subjectInitials.text = item.subjectInitials()
        binding.reportSubject.text = item.subject
        binding.reportContent.text = item.detail
        binding.noEvidenceNotice.isVisible = !item.hasEvidence

        val canClaim = item.status == ReportStatus.NEW || item.status == ReportStatus.RETURNED
        binding.acceptButton.setText(if (canClaim) R.string.mod_btn_accept_and_view else R.string.mod_evidence_title)
        binding.acceptButton.setOnClickListener {
            if (!canClaim) {
                openReport(ReportEvidenceActivity::class.java, item)
                return@setOnClickListener
            }
            val updated = reports.apply(item.id, ReportAction.CLAIM, "")
            if (updated != null) {
                openReport(ReportEvidenceActivity::class.java, updated)
            } else {
                message(R.string.mod_report_update_failed)
                refresh()
            }
        }
    }
}
