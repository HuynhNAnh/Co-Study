package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus
import com.ute.co_study.shared.model.ReportAction

class ModerationReportActivity : ModerationActivity(R.layout.activity_moderation_report) {
    override val refreshOnReturn = true

    override fun render() {
        val item = report() ?: return
        describe(item)
        info("Người gửi báo cáo", "${item.sender} · ${item.createdAt}")
        reportPerson(item)
        info("Nội dung phản ánh", item.detail)
        item.lastEvent?.let { info(it.action.label, it.reason.ifBlank { it.at }) }
        if (!item.hasEvidence) note("Báo cáo chưa kèm tài liệu chứng minh. Kiểm tra nội dung trước khi quyết định.")
        when (item.status) {
            ReportStatus.NEW, ReportStatus.RETURNED -> button("Tiếp nhận & xem bằng chứng") {
                val updated = reports.apply(item.id, ReportAction.CLAIM, "")
                if (updated != null) openReport(ReportEvidenceActivity::class.java, updated) else refresh()
            }
            else -> button("Xem bằng chứng", Tone.PLAIN) { openReport(ReportEvidenceActivity::class.java, item) }
        }
    }
}
