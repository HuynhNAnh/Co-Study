package com.ute.co_study.feature.moderation.ui.report

import com.ute.co_study.databinding.ActivityInfoRequestedBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.R
import com.ute.co_study.feature.moderation.ui.overview.ModerationOverviewActivity
import com.ute.co_study.shared.model.ReportStatus

class InfoRequestedActivity : ModerationActivity<ActivityInfoRequestedBinding>(ActivityInfoRequestedBinding::inflate) {
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
        val item = currentReport(ReportStatus.NEEDS_INFO) ?: return
        describe(item)
        binding.resultDescription.text = getString(R.string.mod_request_sent_to, item.sender)
        binding.resultDetail.text = item.lastEvent?.reason.orEmpty()
    }
}
