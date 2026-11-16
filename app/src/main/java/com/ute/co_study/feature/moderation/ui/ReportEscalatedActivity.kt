package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus

class ReportEscalatedActivity : ModerationActivity(R.layout.activity_report_escalated) {
    override fun render() {
        val item = report(ReportStatus.ESCALATED) ?: return
        describe(item)
        result("Đã gửi hồ sơ xem xét", "Lý do chuyển và bằng chứng đã được đính kèm.")
        info("Tình trạng xử lý", "Chờ Admin quyết định")
        info("Lý do chuyển", item.lastEvent?.reason.orEmpty())
        button("Về danh sách báo cáo") { returnTo(ModerationQueueActivity::class.java) }
        button("Xem thông báo", Tone.PLAIN) { open(ModerationNotificationsActivity::class.java) }
    }
}
