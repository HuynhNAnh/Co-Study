package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.data.DemoMemberDirectory
import com.ute.co_study.shared.data.DemoRole
import com.ute.co_study.shared.ui.StaffLoginActivity

class ModeratorLoginActivity : StaffLoginActivity(
    R.layout.activity_moderator_login,
    DemoRole.MODERATOR,
    "moderator@costudy.example",
    "KDV-01",
    ModerationDashboardActivity::class.java
) {
    override fun resolveOperator(email: String): String? =
        super.resolveOperator(email) ?: DemoMemberDirectory(this).findByEmail(email)
            ?.takeIf { it.role == DemoRole.MODERATOR && !it.locked }?.id
}
