package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudyStatus

class ReviewPartnerActivity : StudyActivity(R.layout.activity_review_partner) {
    override fun render() {
        val item = expect(StudyStatus.COMPLETED) ?: return
        if (item.stars > 0) {
            openSession(ReviewSentActivity::class.java, item.id)
            finish()
            return
        }
        partnerCard(item.partner)
        card("Bạn thấy buổi học thế nào?")
        val rating = choice("Mức độ hài lòng", listOf("1 ★", "2 ★", "3 ★", "4 ★", "5 ★"), "5 ★")
        val strengths = input("Điều bạn ấn tượng", hint = "Đúng giờ, nhiệt tình…", maxLength = 120)
        val comments = input("Nhận xét của bạn", multiline = true)
        note("Nhận xét của bạn giúp cộng đồng tìm được bạn học phù hợp hơn.")
        button("Gửi đánh giá") {
            val review = listOf(strengths.text.toString().trim(), comments.text.toString().trim())
                .filter(String::isNotEmpty).joinToString("\n")
            changed(study.review(item.id, rating.text.first().digitToInt(), review), ReviewSentActivity::class.java)
        }
    }
}
