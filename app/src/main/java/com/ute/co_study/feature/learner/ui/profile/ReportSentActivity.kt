package com.ute.co_study.feature.learner.ui.profile

import com.ute.co_study.R
import com.ute.co_study.feature.learner.ui.home.HomeActivity
import com.ute.co_study.shared.model.ReportStatus
import com.ute.co_study.shared.model.ReportAction

class ReportSentActivity : ProfileActivity(R.layout.activity_report_sent) {
    override fun render() {
        val id = intent.getStringExtra(ProfileActivity.REPORT_ID)
        val item = reports.ownReports().firstOrNull { it.id == id }
        if (item == null) {
            finish()
            return
        }
        result("Cảm ơn bạn đã phản hồi", "Bạn có thể theo dõi cập nhật về báo cáo này trong mục Thông báo.")
        info("Mã báo cáo", item.id)
        info("Trạng thái", item.status.label)
        item.lastEvent?.let { info(it.action.label, it.reason.ifBlank { it.at }) }
        if (item.status == ReportStatus.NEEDS_INFO) {
            val evidence = input("Thông tin bổ sung", multiline = true)
            button("Gửi thông tin bổ sung") {
                val text = required(evidence) ?: return@button
                if (reports.apply(item.id, ReportAction.SUPPLY_INFO, text) != null) refresh()
            }
        }
        button("Về trang chủ") { returnTo(HomeActivity::class.java) }
        button("Quay lại", Tone.PLAIN) { finish() }
    }
}
