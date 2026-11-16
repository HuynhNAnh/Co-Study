package com.ute.co_study.feature.learner.ui.profile

import com.ute.co_study.R
import com.ute.co_study.feature.learner.ui.progress.BadgesActivity
import com.ute.co_study.feature.learner.ui.progress.StreakActivity

class LearnerProfileActivity : ProfileActivity(R.layout.activity_learner_profile) {
    override val refreshOnReturn = true

    override fun render() {
        val draft = profiles.load(session.email)
        identity("Hồ sơ người dùng")
        reputationMetrics()
        card("Hồ sơ kỹ năng", "Chia sẻ: ${value(draft.skillsToShare)}\nMuốn học: ${value(draft.skillsToLearn)}")
        if (draft.skillsToShare.isNotBlank()) {
            val proof = profiles.evidence(session.email)
            val detail = proof?.let {
                listOf(getString(R.string.skill_evidence_status, it.status.label), it.reviewNote)
                    .filter(String::isNotBlank).joinToString("\n")
            } ?: getString(R.string.skill_evidence_not_submitted)
            card(getString(R.string.skill_evidence_title), detail)
        }
        row("Chỉnh sửa hồ sơ", "Kỹ năng, mục tiêu và lịch rảnh") { open(EditLearnerProfileActivity::class.java) }
        row("Uy tín & đánh giá", "Xem phản hồi về các buổi học") { open(ReputationActivity::class.java) }
        row("Chuỗi học tập", "${study.progress().current} ngày liên tiếp") { open(StreakActivity::class.java) }
        val badge = study.featuredBadge()
        row("Bộ sưu tập huy hiệu", badge?.title ?: "Theo dõi các cột mốc học tập") { open(BadgesActivity::class.java) }
        row("Cài đặt tài khoản", "Thông tin tài khoản và đăng xuất") { open(LearnerSettingsActivity::class.java) }
        learnerTabs(4)
    }
}
