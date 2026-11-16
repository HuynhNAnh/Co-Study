package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R

class AdminConfigSavedActivity : AdminActivity(R.layout.activity_admin_config_saved) {
    override fun render() {
        val config = admin.config()
        result("Đã cập nhật cấu hình", "${config.dailyInvites} lời mời mỗi ngày · ${config.reportHours} giờ bổ sung báo cáo")
        card("Quy tắc ghi nhận", "${config.studyMinutes} phút trở lên · xác nhận trong ${config.studyConfirmHours} giờ.")
        card("Điều kiện huy hiệu", "${config.badgeDays.joinToString(", ")} ngày liên tiếp hoặc ${config.partnerSessions} buổi cùng một bạn.")
        button("Về tổng quan") { returnTo(AdminDashboardActivity::class.java) }
    }
}
