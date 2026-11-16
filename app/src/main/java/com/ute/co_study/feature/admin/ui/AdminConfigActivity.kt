package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.CommunityRules

class AdminConfigActivity : AdminActivity(R.layout.activity_admin_config) {
    override fun render() {
        val config = admin.config()
        val invites = integerInput("Giới hạn lời mời ghép cặp mỗi ngày", config.dailyInvites)
        val reports = integerInput("Hạn đề nghị bổ sung báo cáo (giờ)", config.reportHours)
        val minutes = integerInput("Thời lượng tối thiểu của buổi học (phút)", config.studyMinutes)
        val hours = integerInput("Thời hạn xác nhận buổi học (giờ)", config.studyConfirmHours)
        val badges = input("Mốc huy hiệu chuỗi (ngày)", config.badgeDays.joinToString(", "), maxLength = 18)
        val partners = integerInput("Số buổi hợp lệ cùng một bạn để đạt huy hiệu", config.partnerSessions)
        val reason = input("Lý do thay đổi", multiline = true)
        note("Cấu hình chuỗi học tập áp dụng từ lần ghi nhận buổi học tiếp theo.")
        button("Xác nhận lưu cấu hình") {
            val daily = integer(invites, 1..100) ?: return@button
            val reportHours = integer(reports, 1..720) ?: return@button
            val studyMinutes = integer(minutes, 1..240) ?: return@button
            val confirmHours = integer(hours, 1..720) ?: return@button
            val partnerSessions = integer(partners, 1..100) ?: return@button
            val badgeDays = badges.text.toString().split(',').mapNotNull { it.trim().toIntOrNull() }
            val updated = CommunityRules(daily, reportHours, studyMinutes, confirmHours, badgeDays, partnerSessions)
            if (!updated.valid || badges.text.toString().split(',').size != 3) {
                badges.error = "Nhập 3 mốc tăng dần từ 1 đến 365, cách nhau bởi dấu phẩy."
                badges.requestFocus()
                return@button
            }
            if (updated == config) {
                message("Chưa có thay đổi cấu hình")
                return@button
            }
            val explanation = required(reason) ?: return@button
            if (admin.saveConfig(updated, explanation)) {
                open(AdminConfigSavedActivity::class.java)
                finish()
            } else actionFailed()
        }
        button("Hủy thay đổi", Tone.PLAIN) { finish() }
    }
}
