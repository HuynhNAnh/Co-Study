package com.ute.co_study.feature.learner.ui.progress

import com.ute.co_study.R
import com.ute.co_study.shared.model.LocalStudyDay
import com.ute.co_study.feature.learner.ui.study.StudyActivity

class BadgeDetailsActivity : StudyActivity(R.layout.activity_badge_details) {
    override fun render() {
        val key = intent.getStringExtra(BADGE_KEY)
        val badge = study.progress().badges.firstOrNull { it.key == key } ?: return finish()
        if (badge.earned) result(badge.title, "Đã mở khóa ngày ${LocalStudyDay.format(badge.earnedAt)}")
        else card(badge.title, "Chưa mở khóa")
        info("Điều kiện", badge.requirement)
        if (badge.partner.isNotBlank()) info("Bạn học cùng", badge.partner)
        if (badge.earned) {
            val featured = study.featuredBadge()?.key == badge.key
            button(if (featured) "Gỡ khỏi hồ sơ" else "Hiển thị trên hồ sơ") {
                study.featureBadge(if (featured) null else badge.key)
                refresh()
            }
        }
        button("Về bộ sưu tập", Tone.PLAIN) { finish() }
    }
}
