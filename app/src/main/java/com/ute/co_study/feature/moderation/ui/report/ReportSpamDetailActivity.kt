package com.ute.co_study.feature.moderation.ui.report

import com.ute.co_study.databinding.ActivityReportSpamDetailBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.feature.moderation.ui.showAdminRequest
import com.ute.co_study.feature.moderation.ui.subjectInitials
import com.ute.co_study.shared.model.ReportStatus

class ReportSpamDetailActivity : ModerationActivity<ActivityReportSpamDetailBinding>(ActivityReportSpamDetailBinding::inflate) {
    override fun bind() = Unit

    override fun refresh() {
        val item = currentReport(ReportStatus.IN_REVIEW) ?: return
        if (!item.hasEvidence) {
            openReport(ReportEvidenceActivity::class.java, item)
            finish()
            return
        }

        describe(item)
        binding.adminRequest.showAdminRequest(item)
        binding.screenTitle.text = item.title
        binding.subjectInitials.text = item.subjectInitials()
        binding.reportSubject.text = item.subject
        binding.reportSender.text = listOf(item.sender, item.createdAt).filter(String::isNotBlank).joinToString(" · ")
        binding.reportEvidence.text = item.evidence
        binding.reportContent.text = item.detail
        binding.chooseActionButton.setOnClickListener { openReport(ActionOptionsActivity::class.java, item) }
        binding.escalateAdminButton.setOnClickListener { openReport(EscalateAdminActivity::class.java, item) }
        binding.viewEvidenceButton.setOnClickListener { openReport(ReportEvidenceActivity::class.java, item) }
    }
}
