package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus

class AdminAccountDetailsActivity : AdminActivity(R.layout.activity_admin_account_details) {
    override val refreshOnReturn = true

    override fun render() {
        val account = member() ?: return
        val linked = admin.reports.queue().filter { it.subject == account.name }
        status("${account.id} · ${account.name}")
        memberCard(account)
        metrics(linked.size.toString() to "Chuyển cấp", linked.count { it.status == ReportStatus.ESCALATED }.toString() to "Chờ xử lý")
        if (account.locked) info("Đang tạm khóa đến ${formattedDate(account.lockedUntil)}", account.lockReason)
        linked.forEach { item ->
            row(item.id, "${item.title} · ${item.status.label}") { openReport(AdminReportDetailsActivity::class.java, item) }
        }
        if (linked.isEmpty()) note("Tài khoản chưa có báo cáo chuyển cấp.")
        if (account.locked) {
            button("Xem xét mở khóa") { openMember(AdminUnlockActivity::class.java, account, null) }
        } else {
            button("Xem xét khóa tài khoản", Tone.DANGER) {
                val activeReport = linked.firstOrNull { it.id == intent.getStringExtra(ADMIN_REPORT) && it.status == ReportStatus.ESCALATED }
                openMember(AdminLockActivity::class.java, account, activeReport?.id)
            }
            button("Phân quyền", Tone.PLAIN) { openMember(AdminRoleActivity::class.java, account, null) }
        }
    }
}
