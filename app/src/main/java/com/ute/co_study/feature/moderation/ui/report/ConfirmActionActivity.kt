package com.ute.co_study.feature.moderation.ui.report

import com.ute.co_study.databinding.ActivityConfirmActionBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus
import com.ute.co_study.shared.model.ReportAction

class ConfirmActionActivity : ModerationActivity<ActivityConfirmActionBinding>(ActivityConfirmActionBinding::inflate) {
    override fun bind() {
        val item = currentReport(ReportStatus.IN_REVIEW) ?: return
        val reason = intent.getStringExtra(ActionOptionsActivity.REASON) ?: return finish()
        val notice = intent.getStringExtra(ActionOptionsActivity.NOTICE) ?: return finish()
        val explanation = "$reason\n\n${getString(R.string.mod_action_notice, notice)}"
        describe(item)
        binding.affectedContent.text = "${item.subject} · ${item.title}"
        binding.actionGrounds.text = explanation
        binding.backOptionsButton.setOnClickListener { finish() }
        binding.applyActionButton.setOnClickListener {
            updateReport(item, ReportAction.WARN, explanation, ActionCompletedActivity::class.java)
        }
    }
}
