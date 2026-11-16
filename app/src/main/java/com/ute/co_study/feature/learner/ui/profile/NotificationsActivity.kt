package com.ute.co_study.feature.learner.ui.profile

import com.ute.co_study.R
import com.ute.co_study.feature.learner.ui.home.HomeActivity
import android.os.Bundle
import com.ute.co_study.feature.learner.ui.study.ConversationActivity
import com.ute.co_study.feature.learner.ui.study.StudyActivity

class NotificationsActivity : ProfileActivity(R.layout.activity_learner_notifications) {
    override val refreshOnReturn = true

    override fun render() {
        if (session.connected) {
            row("Minh Anh đã đồng ý kết nối", "Bạn có thể bắt đầu nhắn tin và đặt lịch.") {
                open(ConversationActivity::class.java)
            }
        }
        study.sessions().asReversed().forEach { item ->
            row(item.status.label, "${item.subject} · ${item.slot.display}") {
                open(StudyActivity.destination(item), Bundle().apply { putLong(StudyActivity.SESSION_ID, item.id) })
            }
        }
        reports.ownReports().forEach { item ->
            row(item.status.label, "${item.id} · ${item.title}") {
                open(ReportSentActivity::class.java, Bundle().apply { putString(ProfileActivity.REPORT_ID, item.id) })
            }
        }
        row("Hồ sơ giúp ghép cặp tốt hơn", "Cập nhật kỹ năng và thời gian rảnh.") {
            open(EditLearnerProfileActivity::class.java)
        }
        button("Về trang chủ", Tone.PLAIN) { returnTo(HomeActivity::class.java) }
    }
}
