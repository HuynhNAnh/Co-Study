package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus
import com.ute.co_study.shared.model.ReportAction

class DismissReportActivity : ModerationActivity(R.layout.activity_moderation_dismiss_report) {
    override fun render() {
        val item = report(ReportStatus.IN_REVIEW) ?: return
        describe(item)
        info("Kết luận", "Chưa đủ căn cứ xác định vi phạm")
        val reason = input("Lý do bác bỏ", hint = "Giải thích vì sao chưa đủ căn cứ", multiline = true)
        note("Báo cáo sẽ đóng và người gửi xem được lý do trong Thông báo.")
        button("Xác nhận bác bỏ") {
            val text = required(reason) ?: return@button
            applyAction(item, ReportAction.DISMISS, text, ReportDismissedActivity::class.java)
        }
        button("Quay lại xem xét", Tone.PLAIN) { finish() }
    }
}
