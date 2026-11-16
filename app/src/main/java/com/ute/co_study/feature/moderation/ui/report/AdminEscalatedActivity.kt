package com.ute.co_study.feature.moderation.ui.report

import com.ute.co_study.databinding.ActivityAdminEscalatedBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.feature.moderation.ui.history.ModerationNotificationsActivity
import com.ute.co_study.shared.model.ReportStatus

class AdminEscalatedActivity : ModerationActivity<ActivityAdminEscalatedBinding>(ActivityAdminEscalatedBinding::inflate) {
    override fun bind() {
        binding.backToListButton.setOnClickListener {
            returnTo(ReportListActivity::class.java)
            finish()
        }
        binding.viewNotificationsButton.setOnClickListener { open(ModerationNotificationsActivity::class.java) }
    }

    override fun refresh() {
        val item = currentReport(ReportStatus.ESCALATED) ?: return
        describe(item)
        binding.resultDetail.text = item.lastEvent?.reason.orEmpty()
    }
}
