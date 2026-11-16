package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus
import android.os.Bundle

class ModerationActionActivity : ModerationActivity(R.layout.activity_moderation_action) {
    override fun render() {
        val item = report(ReportStatus.IN_REVIEW) ?: return
        describe(item)
        info("Biện pháp đề xuất", "Ẩn nội dung vi phạm và gửi cảnh báo")
        val reason = input("Lý do", item.detail, multiline = true)
        val warning = input("Thông báo cho người đăng", "Vui lòng cập nhật nội dung phù hợp với hoạt động học tập.", multiline = true)
        note("Biện pháp áp dụng cho nội dung được báo cáo. Việc khóa tài khoản cần Admin xem xét.")
        button("Xem lại trước khi xác nhận") {
            val text = required(reason) ?: return@button
            val notice = required(warning) ?: return@button
            open(ConfirmModerationActionActivity::class.java, Bundle().apply {
                putString(ModerationActivity.REPORT_ID, item.id)
                putString("decision_reason", text)
                putString("decision_warning", notice)
            })
        }
        button("Chuyển Admin", Tone.PLAIN) { openReport(EscalateReportActivity::class.java, item) }
    }
}
