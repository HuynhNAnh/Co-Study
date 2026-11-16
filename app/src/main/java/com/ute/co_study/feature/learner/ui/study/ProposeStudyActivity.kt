package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudySlot

class ProposeStudyActivity : StudyActivity(R.layout.activity_propose_study) {
    override fun render() {
        val partner = conversation()
        partnerCard(partner.title)
        val subject = input("Chủ đề học", "Kotlin Activity & Intent", maxLength = 100)
        val slot = slotFields(StudySlot("08/10/2026", "19:00", "20:00"))
        val mode = choice("Hình thức", listOf("Trực tuyến · Google Meet", "Trực tuyến · Khác", "Gặp trực tiếp"))
        note("Lịch được xác nhận khi bạn học đồng ý với đề xuất.")
        button("Gửi đề xuất lịch học") {
            val title = required(subject) ?: return@button
            val time = readSlot(slot) ?: return@button
            changed(study.propose(partner.title, title, time, mode.text.toString()), PendingStudyActivity::class.java)
        }
    }
}
