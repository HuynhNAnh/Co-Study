package com.ute.co_study.feature.learner.ui.progress

import com.ute.co_study.R
import com.ute.co_study.shared.model.LocalStudyDay
import android.os.Bundle
import com.ute.co_study.feature.learner.model.StudyStatus
import com.ute.co_study.feature.learner.ui.profile.ReportPartnerActivity
import com.ute.co_study.feature.learner.ui.study.StudyActivity

class StudyRecognitionActivity : StudyActivity(R.layout.activity_study_recognition) {
    override val refreshOnReturn = true

    override fun render() {
        val item = expect(StudyStatus.COMPLETED) ?: return
        val now = System.currentTimeMillis()
        partnerCard(item.partner)
        sessionCard(item)
        info("Kết quả ghi nhận", item.recognitionStatus(now))
        info("Ngày hoàn thành", LocalStudyDay.format(item.completedAt))
        info("Xác nhận của bạn học", if (item.partnerConfirmedAt > 0) LocalStudyDay.format(item.partnerConfirmedAt) else "Chưa xác nhận")
        note("Quy tắc áp dụng: ${item.policy.minimumMinutes} phút và xác nhận trong ${item.policy.confirmationHours} giờ.")
        if (item.canConfirm(now)) {
            button("Mô phỏng bạn học xác nhận tham gia") {
                if (study.confirmAttendance(item.id) != null) refresh() else message("Đã hết hạn xác nhận")
            }
        }
        button("Gửi phản ánh", Tone.PLAIN) {
            open(ReportPartnerActivity::class.java, Bundle().apply { putLong(StudyActivity.SESSION_ID, item.id) })
        }
    }
}
