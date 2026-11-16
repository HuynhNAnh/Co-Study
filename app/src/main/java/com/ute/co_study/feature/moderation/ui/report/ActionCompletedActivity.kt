package com.ute.co_study.feature.moderation.ui.report

import com.ute.co_study.databinding.ActivityActionCompletedBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.feature.moderation.ui.overview.ModerationOverviewActivity
import com.ute.co_study.shared.model.ReportStatus

class ActionCompletedActivity : ModerationActivity<ActivityActionCompletedBinding>(ActivityActionCompletedBinding::inflate) {
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
        val item = currentReport(ReportStatus.RESOLVED) ?: return
        describe(item)
        val event = item.lastEvent ?: return
        binding.actionActor.text = event.actor
        binding.actionLog.text = "${event.at}\n${event.reason}"
    }
}
