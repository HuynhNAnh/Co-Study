package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus

class AdminReportReturnedActivity : AdminActivity(R.layout.activity_admin_report_returned) {
    override fun render() {
        val item = report(ReportStatus.RETURNED) ?: return
        status(item.id)
        result("Chờ kiểm duyệt bổ sung", "Yêu cầu đã được chuyển vào hàng đợi kiểm duyệt.")
        card("Nội dung yêu cầu", item.lastEvent?.reason.orEmpty())
        button("Về tổng quan") { returnTo(AdminDashboardActivity::class.java) }
    }
}
