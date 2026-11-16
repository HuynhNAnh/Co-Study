package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudySlot
import com.ute.co_study.shared.model.LocalStudyDay

class ProposeStudyActivity : StudyActivity(R.layout.activity_propose_study) {
    override fun render() {
        val partner = conversation()
        val topic = partner.studyTopic ?: return finish()
        partnerCard(partner.title)
        val subject = input("Chủ đề học", topic, maxLength = 100)
        val tomorrow = LocalStudyDay.format(System.currentTimeMillis(), daysAfter = 1)
        val slot = slotFields(StudySlot(tomorrow, "19:00", "20:00"))
        val mode = choice("Hình thức", listOf("Trực tuyến · Google Meet", "Trực tuyến · Khác", "Gặp trực tiếp"))
        note("Lịch được xác nhận khi bạn học đồng ý với đề xuất.")
        button("Gửi đề xuất lịch học") {
            val title = required(subject) ?: return@button
            val time = readSlot(slot) ?: return@button
            changed(study.propose(partner.title, title, time, mode.text.toString()), PendingStudyActivity::class.java)
        }
    }
}
