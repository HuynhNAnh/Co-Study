package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus

class AdminReportReturnActivity : AdminActivity(R.layout.activity_admin_report_return) {
    override fun render() {
        val item = report(ReportStatus.ESCALATED) ?: return
        val request = input("Nội dung yêu cầu", "Bổ sung mốc thời gian và đối chiếu với các quyết định trước đó.", multiline = true)
        val deadline = input("Hạn phản hồi", "Trong ${admin.config().reportHours} giờ", maxLength = 100)
        card("Trạng thái sau khi gửi", "${ReportStatus.RETURNED.label}. Yêu cầu được lưu trong lịch sử vụ việc.")
        button("Xác nhận chuyển lại") {
            val text = required(request) ?: return@button
            val due = required(deadline) ?: return@button
            if (admin.returnReport(item.id, "$text\nHạn phản hồi: $due")) {
                openReport(AdminReportReturnedActivity::class.java, item)
                finish()
            } else actionFailed()
        }
        button("Hủy yêu cầu", Tone.PLAIN) { finish() }
    }
}
