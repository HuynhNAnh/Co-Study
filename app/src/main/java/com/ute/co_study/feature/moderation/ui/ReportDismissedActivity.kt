package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus

class ReportDismissedActivity : ModerationActivity(R.layout.activity_report_dismissed) {
    override fun render() {
        val item = report(ReportStatus.DISMISSED) ?: return
        describe(item)
        result("Đã ghi nhận kết luận", "Người gửi xem được lý do đóng báo cáo trong Thông báo.")
        info("Kết luận đã lưu", item.lastEvent?.reason.orEmpty())
        note("Khi có bằng chứng mới, người dùng có thể gửi báo cáo mới kèm thông tin bổ sung.")
        resolutionActions()
    }
}
