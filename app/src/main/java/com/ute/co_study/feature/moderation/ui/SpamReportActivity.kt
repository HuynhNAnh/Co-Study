package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus

class SpamReportActivity : ModerationActivity(R.layout.activity_spam_report) {
    override val refreshOnReturn = true

    override fun render() {
        val item = report(ReportStatus.IN_REVIEW) ?: return
        describe(item)
        reportPerson(item)
        info("Người báo cáo", "${item.sender} · ${item.createdAt}")
        card("Bằng chứng · ${item.title}", item.evidence)
        note("Đối chiếu bằng chứng với quy tắc cộng đồng trước khi xử lý.")
        button("Chọn phương án xử lý") { openReport(ModerationActionActivity::class.java, item) }
        button("Chuyển Admin xem xét", Tone.PLAIN) { openReport(EscalateReportActivity::class.java, item) }
        button("Xem chi tiết bằng chứng", Tone.PLAIN) { openReport(ReportEvidenceActivity::class.java, item) }
    }
}
