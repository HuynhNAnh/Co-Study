package com.ute.co_study.feature.moderation.ui.report

import com.ute.co_study.databinding.ActivityRequestMoreInfoBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus
import com.ute.co_study.shared.model.ReportAction
import com.ute.co_study.shared.data.CommunityRulesRepository

class RequestMoreInfoActivity : ModerationActivity<ActivityRequestMoreInfoBinding>(ActivityRequestMoreInfoBinding::inflate) {
    override fun bind() {
        val item = currentReport(ReportStatus.IN_REVIEW) ?: return
        describe(item)
        binding.reportRecipient.text = item.sender
        val hours = CommunityRulesRepository(this).load().reportHours
        binding.replyDeadline.setText(getString(R.string.mod_reply_hours, hours))
        binding.backEvidenceButton.setOnClickListener { finish() }
        binding.sendRequestButton.setOnClickListener {
            val request = required(binding.requestDetailsInput) ?: return@setOnClickListener
            val deadline = required(binding.replyDeadline) ?: return@setOnClickListener
            val detail = "$request\n${getString(R.string.mod_reply_deadline, deadline)}"
            updateReport(item, ReportAction.REQUEST_INFO, detail, InfoRequestedActivity::class.java)
        }
    }
}
