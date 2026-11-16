package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.feature.auth.ui.welcome.WelcomeActivity

class ModeratorSignOutActivity : ModerationActivity(R.layout.activity_moderator_sign_out) {
    override fun render() {
        card("Kết thúc phiên làm việc", "Các quyết định đã xác nhận được lưu trong lịch sử.", Tone.MINT)
        info("Tài khoản hiện tại", "${session.name} · ${session.email}")
        button("Xác nhận đăng xuất") {
            session.end()
            replaceTask(WelcomeActivity::class.java)
        }
        button("Tiếp tục làm việc", Tone.PLAIN) { finish() }
    }
}
