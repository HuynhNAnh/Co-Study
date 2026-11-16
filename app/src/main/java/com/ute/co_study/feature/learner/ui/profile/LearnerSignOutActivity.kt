package com.ute.co_study.feature.learner.ui.profile

import com.ute.co_study.R
import com.ute.co_study.feature.role.ui.RoleSelectionActivity

class LearnerSignOutActivity : ProfileActivity(R.layout.activity_learner_sign_out) {
    override fun render() {
        card("Hẹn gặp lại, ${displayName}!", "Hồ sơ, tin nhắn và lịch học được lưu trên thiết bị.", Tone.MINT)
        identity()
        button("Xác nhận đăng xuất", Tone.DANGER) {
            session.end()
            replaceTask(RoleSelectionActivity::class.java)
        }
        button("Ở lại", Tone.PLAIN) { finish() }
    }
}
