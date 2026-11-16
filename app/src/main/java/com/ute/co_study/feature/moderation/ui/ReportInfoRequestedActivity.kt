package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus

class ReportInfoRequestedActivity : ModerationActivity(R.layout.activity_report_info_requested) {
    override fun render() {
        val item = report(ReportStatus.NEEDS_INFO) ?: return
        describe(item)
        result("Đang chờ người báo cáo", "Yêu cầu đã được lưu cho ${item.sender}.")
        info("Nội dung yêu cầu", item.lastEvent?.reason.orEmpty())
        note("Nội dung yêu cầu, người xử lý và thời điểm được lưu trong lịch sử báo cáo.")
        resolutionActions()
    }
}
