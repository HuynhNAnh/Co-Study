package com.ute.co_study.feature.moderation.ui.report

import com.ute.co_study.databinding.ActivityActionOptionsBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import android.os.Bundle
import com.ute.co_study.shared.model.ReportStatus

class ActionOptionsActivity : ModerationActivity<ActivityActionOptionsBinding>(ActivityActionOptionsBinding::inflate) {
    override fun bind() {
        val item = currentReport(ReportStatus.IN_REVIEW) ?: return
        if (!item.hasEvidence) {
            finish()
            return
        }
        describe(item)
        binding.actionReasonInput.setText(item.detail)
        binding.reviewActionButton.setOnClickListener {
            val reason = required(binding.actionReasonInput) ?: return@setOnClickListener
            val notice = required(binding.authorNoticeInput) ?: return@setOnClickListener
            openReport(ConfirmActionActivity::class.java, item, Bundle().apply {
                putString(ActionOptionsActivity.REASON, reason)
                putString(ActionOptionsActivity.NOTICE, notice)
            })
        }
        binding.escalateAdminButton.setOnClickListener { openReport(EscalateAdminActivity::class.java, item) }
    }

    internal companion object {
        const val REASON = "moderation_decision_reason"
        const val NOTICE = "moderation_author_notice"
    }
}
