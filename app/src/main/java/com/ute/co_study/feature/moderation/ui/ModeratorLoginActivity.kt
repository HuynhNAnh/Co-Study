package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.data.DemoRole
import com.ute.co_study.shared.ui.StaffLoginActivity

class ModeratorLoginActivity : StaffLoginActivity(
    R.layout.activity_moderator_login,
    DemoRole.MODERATOR,
    "moderator@costudy.example",
    "KDV-01",
    ModerationDashboardActivity::class.java
)
