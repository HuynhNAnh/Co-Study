package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus
import com.ute.co_study.shared.model.ReportAction

class EscalateReportActivity : ModerationActivity(R.layout.activity_escalate_report) {
    override fun render() {
        val item = report(ReportStatus.IN_REVIEW) ?: return
        describe(item)
        val reason = input("Lý do chuyển", "Cần xem xét lịch sử vi phạm và quyết định ở cấp tài khoản.", multiline = true)
        info("Hồ sơ chuyển kèm", "${item.id}\n${item.evidence}")
        note("Hồ sơ và lịch sử được chuyển đến Admin để xem xét.")
        button("Xác nhận chuyển Admin") {
            val text = required(reason) ?: return@button
            applyAction(item, ReportAction.ESCALATE, text, ReportEscalatedActivity::class.java)
        }
        button("Quay lại báo cáo", Tone.PLAIN) { finish() }
    }
}
