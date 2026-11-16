package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus
import com.ute.co_study.shared.model.ReportAction

class ConfirmModerationActionActivity : ModerationActivity(R.layout.activity_confirm_moderation_action) {
    override fun render() {
        val item = report(ReportStatus.IN_REVIEW) ?: return
        val reason = intent.getStringExtra("decision_reason") ?: return finish()
        val warning = intent.getStringExtra("decision_warning") ?: return finish()
        describe(item)
        info("Nội dung bị tác động", "${item.subject} · ${item.title}")
        info("Biện pháp áp dụng", "Ẩn nội dung và gửi cảnh báo")
        info("Căn cứ xử lý", reason)
        info("Thông báo cho người đăng", warning)
        note("Quyết định được lưu vào lịch sử. Tài khoản vẫn hoạt động.")
        button("Xác nhận áp dụng", Tone.DANGER) {
            applyAction(item, ReportAction.WARN, "$reason\nThông báo: $warning", ModerationResolvedActivity::class.java)
        }
        button("Quay lại chỉnh phương án", Tone.PLAIN) { finish() }
    }
}
