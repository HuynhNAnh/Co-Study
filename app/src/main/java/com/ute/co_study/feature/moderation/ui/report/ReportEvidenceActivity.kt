package com.ute.co_study.feature.moderation.ui.report

import com.ute.co_study.databinding.ActivityReportEvidenceBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.feature.moderation.ui.showAdminRequest
import com.ute.co_study.R
import androidx.core.view.isVisible
import com.ute.co_study.shared.model.ReportStatus

class ReportEvidenceActivity : ModerationActivity<ActivityReportEvidenceBinding>(ActivityReportEvidenceBinding::inflate) {
    override fun bind() = Unit

    override fun refresh() {
        val item = currentReport() ?: return
        describe(item)
        binding.adminRequest.showAdminRequest(item)
        val canAct = item.status == ReportStatus.IN_REVIEW
        binding.reportContent.text = "${item.subject} · ${item.title}\n${item.detail}"
        binding.reportEvidence.text = item.evidence.ifBlank { getString(R.string.mod_no_evidence) }
        binding.evidenceNotice.isVisible = canAct && !item.hasEvidence
        binding.requestMoreInfoButton.isVisible = canAct
        binding.dismissButton.isVisible = canAct
        binding.requestMoreInfoButton.setOnClickListener { openReport(RequestMoreInfoActivity::class.java, item) }
        binding.dismissButton.setOnClickListener { openReport(DismissReportActivity::class.java, item) }
        binding.escalateAdminButton.isVisible = canAct
        binding.escalateAdminButton.setOnClickListener { openReport(EscalateAdminActivity::class.java, item) }
        binding.chooseActionButton.isVisible = canAct && item.hasEvidence
        binding.chooseActionButton.setOnClickListener { openReport(ActionOptionsActivity::class.java, item) }
    }
}
