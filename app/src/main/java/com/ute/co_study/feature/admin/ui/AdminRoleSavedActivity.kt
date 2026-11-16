package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R
import com.ute.co_study.shared.data.DemoRole

class AdminRoleSavedActivity : AdminActivity(R.layout.activity_admin_role_saved) {
    override fun render() {
        val account = member() ?: return
        result("Đã cập nhật vai trò", "${account.name} · ${account.roleLabel}")
        card("Thông tin ghi nhận", "Người thực hiện: ${session.name}\nLý do: ${intent.getStringExtra(ADMIN_REASON).orEmpty()}")
        if (account.role == DemoRole.MODERATOR) info("Email đăng nhập kiểm duyệt demo", account.email)
        button("Về tổng quan") { returnTo(AdminDashboardActivity::class.java) }
    }
}
