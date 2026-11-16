package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus

class ModerationDashboardActivity : ModerationActivity(R.layout.activity_moderation_dashboard) {
    override val refreshOnReturn = true

    override fun render() {
        val queue = reports.queue()
        label(getString(R.string.workflow_demo))
        metrics(
            queue.count { it.status == ReportStatus.NEW }.toString() to "Mới tiếp nhận",
            queue.count { it.status == ReportStatus.IN_REVIEW }.toString() to "Đang xử lý",
            queue.count { it.status == ReportStatus.ESCALATED }.toString() to "Chờ Admin"
        )
        row("Thông báo kiểm duyệt", "Cập nhật công việc của bạn") { open(ModerationNotificationsActivity::class.java) }
        val active = queue.filter { it.status in setOf(ReportStatus.NEW, ReportStatus.IN_REVIEW, ReportStatus.RETURNED) }
        active.take(3).forEach { reportRow(it) }
        if (active.isEmpty()) note("Hàng đợi hiện không có báo cáo cần tiếp nhận.")
        button("Xem tất cả báo cáo", parent = body) { returnTo(ModerationQueueActivity::class.java) }
        moderationTabs(0)
    }
}
