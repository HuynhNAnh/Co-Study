package com.ute.co_study.feature.moderation.ui.report

import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityEscalateAdminBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.feature.moderation.ui.showAdminRequest
import com.ute.co_study.shared.model.ReportAction
import com.ute.co_study.shared.model.ReportStatus

class EscalateAdminActivity : ModerationActivity<ActivityEscalateAdminBinding>(ActivityEscalateAdminBinding::inflate) {
    override fun bind() {
        val item = currentReport(ReportStatus.IN_REVIEW) ?: return
        describe(item)
        binding.adminRequest.showAdminRequest(item)
        val currentEvidence = item.evidence.ifBlank { getString(R.string.mod_no_evidence) }
        binding.escalationEvidence.text = "${item.id}\n$currentEvidence"
        binding.supportingEvidenceLabel.setText(
            if (item.hasEvidence) R.string.mod_escalation_evidence_optional else R.string.mod_escalation_evidence_required
        )
        binding.backReportButton.setOnClickListener { finish() }
        binding.confirmEscalateButton.setOnClickListener {
            val reason = required(binding.escalationReasonInput) ?: return@setOnClickListener
            val evidence = if (item.hasEvidence) binding.supportingEvidenceInput.text.toString().trim()
                else required(binding.supportingEvidenceInput) ?: return@setOnClickListener
            updateReport(item, ReportAction.ESCALATE, reason, AdminEscalatedActivity::class.java, evidence)
        }
    }
}
