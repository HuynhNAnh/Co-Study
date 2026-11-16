package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus

class AdminReportDetailsActivity : AdminActivity(R.layout.activity_admin_report_details) {
    override val refreshOnReturn = true

    override fun render() {
        val item = report() ?: return
        status("${item.id} · ${item.status.label}")
        card(item.subject, "Người báo cáo: ${item.sender}\n${item.detail}")
        card("Bằng chứng", item.evidence.ifBlank { "Chưa có bằng chứng đính kèm." })
        item.history.asReversed().forEach { event ->
            info(event.action.label, "${event.actor} · ${event.at}\n${event.reason}".trimEnd())
        }
        val account = admin.accounts().firstOrNull { it.name == item.subject }
        if (account != null) row("Xem lịch sử tài khoản", "${account.id} · ${account.status}") {
            openMember(AdminAccountDetailsActivity::class.java, account, item.id)
        }
        if (item.status == ReportStatus.ESCALATED) {
            button("Trả kết quả") { openReport(AdminReportResolveActivity::class.java, item) }
            if (account != null && !account.locked) {
                button("Xem xét khóa tài khoản", Tone.DANGER) { openMember(AdminLockActivity::class.java, account, item.id) }
            }
            button("Yêu cầu bổ sung", Tone.PLAIN) { openReport(AdminReportReturnActivity::class.java, item) }
        }
    }
}
