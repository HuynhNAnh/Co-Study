package com.ute.co_study.feature.learner.ui.profile

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.ProfileSetupDraft

class EditLearnerProfileActivity : ProfileActivity(R.layout.activity_edit_learner_profile) {
    override fun render() {
        val draft = profiles.load(session.email)
        val name = input("Họ và tên", session.name, maxLength = 100)
        val share = input("Kỹ năng có thể chia sẻ", draft.skillsToShare)
        val learn = input("Kỹ năng muốn học", draft.skillsToLearn)
        val goal = input("Mục tiêu học tập", draft.learningGoal, multiline = true)
        val availability = input("Lịch rảnh", draft.availability, multiline = true)
        note("Hồ sơ giúp Co-Study gợi ý những kết nối học tập phù hợp.")
        button("Lưu thay đổi") {
            val displayName = required(name) ?: return@button
            val updated = ProfileSetupDraft(
                share.text.toString().trim(), learn.text.toString().trim(),
                goal.text.toString().trim(), availability.text.toString().trim()
            )
            if (profiles.save(session.email, updated)) {
                session.updateName(displayName)
                message("Đã lưu hồ sơ")
                finish()
            } else message("Chưa thể lưu hồ sơ")
        }
        button("Hủy thay đổi", Tone.PLAIN) { finish() }
    }
}
