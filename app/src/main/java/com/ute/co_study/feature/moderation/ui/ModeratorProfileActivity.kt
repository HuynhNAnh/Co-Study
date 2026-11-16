package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R

class ModeratorProfileActivity : ModerationActivity(R.layout.activity_moderation_account) {
    override fun render() {
        status("${session.name} · Đang hoạt động")
        person("KD", "Kiểm duyệt viên", session.email)
        card("Quyền được cấp", "Xem báo cáo và bằng chứng.\nYêu cầu bổ sung hoặc bác bỏ có lý do.\nẨn nội dung vi phạm và gửi cảnh báo.\nChuyển Admin và tra cứu lịch sử.")
        note("Admin quản lý các quyết định ở cấp tài khoản.")
        button("Đăng xuất", Tone.PLAIN, body) { open(ModeratorSignOutActivity::class.java) }
        moderationTabs(3)
    }
}
