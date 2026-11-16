package com.ute.co_study.feature.learner.ui.profile

import android.os.Bundle
import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudyStatus
import com.ute.co_study.feature.learner.ui.study.StudyActivity

class ReportPartnerActivity : ProfileActivity(R.layout.activity_report_partner) {
    override fun render() {
        val sessionId = intent.getLongExtra(StudyActivity.SESSION_ID, -1L)
        val studyItem = study.find(sessionId)?.takeIf { it.status == StudyStatus.COMPLETED }
        if (intent.hasExtra(StudyActivity.SESSION_ID) && studyItem == null) {
            message("Không tìm thấy buổi học đã hoàn thành để gửi phản ánh")
            finish()
            return
        }
        val subject = studyItem?.partner ?: "Minh Anh"
        if (studyItem == null) person("MA", subject, "Hồ sơ được báo cáo")
        else info("Buổi học được phản ánh", "${studyItem.subject} · ${studyItem.slot.display} · $subject")
        val reasons = if (studyItem != null) listOf("Ghi nhận buổi học", "Thời lượng không chính xác", "Kết quả xác nhận không chính xác")
            else listOf("Thông tin hồ sơ không phù hợp", "Spam hoặc quảng cáo", "Hành vi gây quấy rối", "Lý do khác")
        val reason = choice("Lý do báo cáo", reasons)
        val details = input("Mô tả chi tiết", hint = "Mô tả nội dung cần xem xét", multiline = true)
        val evidence = input("Bằng chứng (không bắt buộc)", hint = "Nhập trích đoạn hoặc liên kết liên quan", multiline = true, maxLength = 1000)
        note("Báo cáo được chuyển vào hàng đợi kiểm duyệt trên thiết bị. Hãy cung cấp thông tin đúng sự thật.")
        button("Gửi báo cáo", Tone.DANGER) {
            val detail = required(details) ?: return@button
            val contextualDetail = if (studyItem == null) detail else
                "${studyItem.subject} · ${studyItem.slot.display}\n${studyItem.recognitionStatus(System.currentTimeMillis())}\n\n$detail"
            val item = reports.submit(reason.text.toString(), subject, contextualDetail, evidence.text.toString())
            if (item == null) {
                message("Chưa thể gửi báo cáo")
                return@button
            }
            open(ReportSentActivity::class.java, Bundle().apply {
                putString(ProfileActivity.REPORT_ID, item.id)
            })
            finish()
        }
        button("Hủy báo cáo", Tone.PLAIN) { finish() }
    }
}
