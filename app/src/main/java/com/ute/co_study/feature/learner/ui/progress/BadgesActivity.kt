package com.ute.co_study.feature.learner.ui.progress

import com.ute.co_study.R
import android.os.Bundle
import com.ute.co_study.feature.learner.ui.study.StudyActivity

class BadgesActivity : StudyActivity(R.layout.activity_badges) {
    override val refreshOnReturn = true

    override fun render() {
        val badges = study.progress().badges
        val featured = study.featuredBadge()?.key
        status("${badges.count { it.earned }} huy hiệu đã mở khóa")
        badges.forEach { badge ->
            val state = when (badge.key) {
                featured -> "Đang hiển thị trên hồ sơ"
                else -> if (badge.earned) "Đã mở khóa" else "Chưa mở khóa"
            }
            row(badge.title, "$state\n${badge.requirement}") {
                open(BadgeDetailsActivity::class.java, Bundle().apply { putString(BADGE_KEY, badge.key) })
            }
        }
    }
}
