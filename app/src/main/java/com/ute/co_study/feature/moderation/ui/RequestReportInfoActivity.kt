package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus
import com.ute.co_study.shared.model.ReportAction

class RequestReportInfoActivity : ModerationActivity(R.layout.activity_request_report_info) {
    override fun render() {
        val item = report(ReportStatus.IN_REVIEW) ?: return
        describe(item)
        info("Người nhận", "${item.sender} · Người gửi báo cáo")
        val request = input("Nội dung yêu cầu", "Vui lòng cung cấp ảnh chụp hoặc nội dung có thể kiểm chứng.", multiline = true)
        val deadline = input("Hạn đề nghị phản hồi", "08/10/2026 · 17:00", maxLength = 80)
        note("Báo cáo chuyển sang Cần bổ sung. Chưa áp dụng biện pháp xử lý.")
        button("Gửi yêu cầu bổ sung") {
            val text = required(request) ?: return@button
            val due = required(deadline) ?: return@button
            applyAction(item, ReportAction.REQUEST_INFO, "$text\nHạn phản hồi: $due", ReportInfoRequestedActivity::class.java)
        }
        button("Quay lại bằng chứng", Tone.PLAIN) { finish() }
    }
}
