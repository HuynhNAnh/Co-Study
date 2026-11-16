package com.ute.co_study.feature.learner.ui.profile

import android.widget.TextView
import com.ute.co_study.R
import com.ute.co_study.feature.learner.ui.home.HomeActivity
import com.ute.co_study.shared.model.ReportAction
import com.ute.co_study.shared.model.ReportEvent
import com.ute.co_study.shared.model.ReportStatus

class ReportSentActivity : ProfileActivity(R.layout.activity_report_sent) {
    override val refreshOnReturn = true

    override fun render() {
        val id = intent.getStringExtra(ProfileActivity.REPORT_ID)
        val item = reports.ownReports().firstOrNull { it.id == id }
        if (item == null) {
            finish()
            return
        }

        if (item.status == ReportStatus.NEW) {
            result("Cảm ơn bạn đã phản hồi", "Bạn có thể theo dõi cập nhật về báo cáo này trong mục Thông báo.")
        } else {
            findViewById<TextView>(R.id.workflow_title).text = "Chi tiết báo cáo"
        }
        info("Mã báo cáo", item.id)
        info("Trạng thái", item.status.label)
        item.history.asReversed().forEach { event -> info(event.at, reporterMessage(event)) }
        if (item.status == ReportStatus.NEEDS_INFO) {
            val evidence = input("Thông tin bổ sung", multiline = true, maxLength = 1000)
            button("Gửi thông tin bổ sung") {
                val text = required(evidence) ?: return@button
                if (reports.apply(item.id, ReportAction.SUPPLY_INFO, text) != null) {
                    message("Đã gửi thông tin bổ sung")
                    refresh()
                } else {
                    message("Chưa thể gửi thông tin bổ sung. Hãy kiểm tra lại trạng thái báo cáo.")
                    refresh(preserveInputs = true)
                }
            }
        }
        button("Về trang chủ") { returnTo(HomeActivity::class.java) }
        button("Quay lại", Tone.PLAIN) { finish() }
    }

    private fun reporterMessage(event: ReportEvent): String = when (event.action) {
        ReportAction.CLAIM -> "Kiểm duyệt viên đã tiếp nhận báo cáo."
        ReportAction.REQUEST_INFO -> "Kiểm duyệt viên yêu cầu bổ sung\n${event.reason}"
        ReportAction.SUPPLY_INFO -> "Bạn đã gửi thông tin bổ sung."
        ReportAction.DISMISS -> "Báo cáo đã đóng\n${event.reason}"
        ReportAction.WARN -> "Kiểm duyệt viên đã ghi nhận biện pháp xử lý."
        ReportAction.ESCALATE -> "Báo cáo đã được chuyển đến Admin."
        ReportAction.RETURN -> "Admin đề nghị kiểm duyệt viên bổ sung hồ sơ."
        ReportAction.LOCK_ACCOUNT -> "Admin đã xử lý báo cáo ở cấp tài khoản."
        ReportAction.RESOLVE_ESCALATION -> "Kết quả từ Admin\n${event.reason}"
    }
}
