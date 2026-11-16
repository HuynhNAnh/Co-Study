package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus

class AdminReportsActivity : AdminActivity(R.layout.activity_admin_reports) {
    override val refreshOnReturn = true

    override fun render() {
        val reports = admin.reports.queue()
        status("${reports.count { it.status == ReportStatus.ESCALATED }} vụ việc chờ Admin xem xét")
        reports.forEach { item ->
            row(item.title, "${item.id} · ${item.status.label}\n${item.subject}") { openReport(AdminReportDetailsActivity::class.java, item) }
        }
        if (reports.isEmpty()) note("Chưa có báo cáo chuyển cấp.")
        adminTabs(2)
    }
}
