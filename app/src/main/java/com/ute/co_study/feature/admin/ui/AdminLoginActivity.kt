package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R
import com.ute.co_study.shared.data.DemoRole
import com.ute.co_study.shared.ui.StaffLoginActivity

class AdminLoginActivity : StaffLoginActivity(
    R.layout.activity_admin_login,
    DemoRole.ADMIN,
    "admin@costudy.example",
    "Admin-01",
    AdminDashboardActivity::class.java
)
