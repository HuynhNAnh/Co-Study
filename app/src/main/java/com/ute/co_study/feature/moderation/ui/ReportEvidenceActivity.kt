package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus

class ReportEvidenceActivity : ModerationActivity(R.layout.activity_report_evidence) {
    override val refreshOnReturn = true

    override fun render() {
        val item = report() ?: return
        describe(item)
        card("Nội dung được phản ánh", "${item.subject}\n${item.detail}")
        info("Bằng chứng", item.evidence.ifBlank { "Chưa có tài liệu hoặc đoạn hội thoại đính kèm." })
        if (item.status != ReportStatus.IN_REVIEW) {
            note("Trạng thái hiện tại: ${item.status.label}")
            button("Quay lại báo cáo", Tone.PLAIN) { finish() }
            return
        }
        if (item.hasEvidence) {
            button("Chọn phương án xử lý") { openReport(ModerationActionActivity::class.java, item) }
            button("Chuyển Admin xem xét", Tone.PLAIN) { openReport(EscalateReportActivity::class.java, item) }
        } else {
            note("Bạn có thể yêu cầu bổ sung hoặc đóng báo cáo với lý do rõ ràng.")
        }
        button("Yêu cầu bổ sung thông tin", if (item.hasEvidence) Tone.PLAIN else Tone.PRIMARY) {
            openReport(RequestReportInfoActivity::class.java, item)
        }
        button("Bác bỏ báo cáo", Tone.PLAIN) { openReport(DismissReportActivity::class.java, item) }
    }
}
