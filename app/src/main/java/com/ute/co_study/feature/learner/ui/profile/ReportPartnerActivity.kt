package com.ute.co_study.feature.learner.ui.profile

import com.ute.co_study.R
import android.os.Bundle

class ReportPartnerActivity : ProfileActivity(R.layout.activity_report_partner) {
    override fun render() {
        person("MA", "Minh Anh", "Hồ sơ được báo cáo")
        val reason = choice("Lý do báo cáo", listOf("Thông tin hồ sơ không phù hợp", "Spam hoặc quảng cáo", "Hành vi gây quấy rối", "Lý do khác"))
        val details = input("Mô tả chi tiết", hint = "Mô tả nội dung cần xem xét", multiline = true)
        note("Báo cáo được chuyển vào hàng đợi kiểm duyệt trên thiết bị. Hãy cung cấp thông tin đúng sự thật.")
        button("Gửi báo cáo", Tone.DANGER) {
            val detail = required(details) ?: return@button
            val item = reports.submit(reason.text.toString(), "Minh Anh", detail) ?: return@button
            open(ReportSentActivity::class.java, Bundle().apply { putString(ProfileActivity.REPORT_ID, item.id) })
            finish()
        }
        button("Hủy, quay lại hồ sơ", Tone.PLAIN) { finish() }
    }
}
