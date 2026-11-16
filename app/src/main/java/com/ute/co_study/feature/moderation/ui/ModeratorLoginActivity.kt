package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.feature.moderation.ui.login.ModeratorLoginActivity as SampleModeratorLoginActivity
import com.ute.co_study.shared.data.DemoMemberDirectory
import com.ute.co_study.shared.data.DemoRole
import com.ute.co_study.shared.data.MODERATOR_DEMO_CODE
import com.ute.co_study.shared.data.MODERATOR_DEMO_EMAIL
import com.ute.co_study.shared.ui.StaffLoginActivity

class ModeratorLoginActivity : StaffLoginActivity(
    R.layout.activity_moderation_email_login,
    DemoRole.MODERATOR,
    MODERATOR_DEMO_EMAIL,
    MODERATOR_DEMO_CODE,
    ModerationDashboardActivity::class.java
) {
    override fun render() {
        super.render()
        button(getString(R.string.mod_sample_entry), Tone.PLAIN) {
            open(SampleModeratorLoginActivity::class.java)
        }
    }

    override fun resolveOperator(email: String): String? =
        super.resolveOperator(email) ?: DemoMemberDirectory(this).findByEmail(email)
            ?.takeIf { it.role == DemoRole.MODERATOR && !it.locked }?.id
}
