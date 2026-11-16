package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudyStatus

class ReviewSentActivity : StudyActivity(R.layout.activity_review_sent) {
    override fun render() {
        val item = expect(StudyStatus.COMPLETED) ?: return
        result("Cảm ơn bạn!", "Đánh giá của bạn đã được ghi nhận. Hẹn gặp bạn trong buổi học tiếp theo.")
        partnerCard(item.partner)
        info("Đánh giá đã lưu", "${item.stars}/5 ★" + item.review.takeIf(String::isNotBlank)?.let { "\n$it" }.orEmpty())
        button("Về danh sách tin nhắn") { returnTo(MessagesActivity::class.java) }
    }
}
