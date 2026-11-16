package com.ute.co_study.feature.learner.ui.profile

import com.ute.co_study.R

class ReputationActivity : ProfileActivity(R.layout.activity_reputation) {
    override fun render() {
        reputationMetrics()
        note("Giữ lịch hẹn, tham gia đầy đủ và trao đổi tôn trọng để xây dựng uy tín với bạn học.")
        label(getString(R.string.workflow_demo))
        card("Minh Anh · ★★★★★", "Bạn chuẩn bị nội dung kỹ và giải thích Figma dễ hiểu. Rất vui được học cùng bạn!")
        card("Hoàng Nam · ★★★★★", "Đúng giờ, nhiệt tình và chủ động chia sẻ tài liệu.")
        button("Về hồ sơ cá nhân", Tone.PLAIN) { finish() }
    }
}
