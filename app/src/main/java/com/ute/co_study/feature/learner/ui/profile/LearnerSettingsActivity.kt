package com.ute.co_study.feature.learner.ui.profile

import com.ute.co_study.R

class LearnerSettingsActivity : ProfileActivity(R.layout.activity_learner_settings) {
    override val refreshOnReturn = true

    override fun render() {
        identity()
        info("Vai trò tài khoản", "Người dùng")
        row("Thông tin hồ sơ", "Cập nhật kỹ năng và lịch rảnh") { open(EditLearnerProfileActivity::class.java) }
        row("Thông báo của tôi", "Lời mời và hoạt động học tập") { open(NotificationsActivity::class.java) }
        note("Tài khoản trình diễn lưu hồ sơ và hoạt động học tập trên thiết bị.")
        button("Đăng xuất", Tone.DANGER) { open(LearnerSignOutActivity::class.java) }
    }
}
