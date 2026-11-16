package com.ute.co_study.feature.learner.ui.profile

import com.ute.co_study.R

class LearnerProfileActivity : ProfileActivity(R.layout.activity_learner_profile) {
    override val refreshOnReturn = true

    override fun render() {
        val draft = profiles.load(session.email)
        identity("Hồ sơ người dùng")
        reputationMetrics()
        card("Hồ sơ kỹ năng", "Chia sẻ: ${value(draft.skillsToShare)}\nMuốn học: ${value(draft.skillsToLearn)}")
        row("Chỉnh sửa hồ sơ", "Kỹ năng, mục tiêu và lịch rảnh") { open(EditLearnerProfileActivity::class.java) }
        row("Uy tín & đánh giá", "Xem phản hồi về các buổi học") { open(ReputationActivity::class.java) }
        row("Cài đặt tài khoản", "Thông tin tài khoản và đăng xuất") { open(LearnerSettingsActivity::class.java) }
        learnerTabs(4)
    }
}
