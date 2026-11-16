package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus

class AdminReportResolveActivity : AdminActivity(R.layout.activity_admin_report_resolve) {
    override fun render() {
        val item = report(ReportStatus.ESCALATED) ?: return
        status("${item.id} · ${item.status.label}")
        card(item.title, "Đối tượng: ${item.subject}")
        val outcome = input("Kết quả gửi người báo cáo", hint = "Nhập kết luận sau khi xem xét hồ sơ", multiline = true)
        note("Kết quả được lưu vào nhật ký và hiển thị cho người báo cáo và kiểm duyệt viên. Xác nhận sẽ kết thúc vụ việc.")
        button("Xác nhận trả kết quả") {
            val result = required(outcome) ?: return@button
            if (admin.resolveReport(item.id, result)) {
                message("Đã trả kết quả báo cáo")
                finish()
            } else actionFailed()
        }
        button("Quay lại báo cáo", Tone.PLAIN) { finish() }
    }
}
