package com.ute.co_study.feature.moderation.ui.report

import com.ute.co_study.databinding.ActivityEscalateAdminBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.shared.model.ReportStatus
import com.ute.co_study.shared.model.ReportAction

class EscalateAdminActivity : ModerationActivity<ActivityEscalateAdminBinding>(ActivityEscalateAdminBinding::inflate) {
    override fun bind() {
        val item = currentReport(ReportStatus.IN_REVIEW) ?: return
        if (!item.hasEvidence) {
            finish()
            return
        }
        describe(item)
        binding.escalationEvidence.text = "${item.id}\n${item.evidence}"
        binding.backReportButton.setOnClickListener { finish() }
        binding.confirmEscalateButton.setOnClickListener {
            val reason = required(binding.escalationReasonInput) ?: return@setOnClickListener
            updateReport(item, ReportAction.ESCALATE, reason, AdminEscalatedActivity::class.java)
        }
    }
}
