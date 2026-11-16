package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R
import com.ute.co_study.shared.data.DemoRole

class AdminRoleActivity : AdminActivity(R.layout.activity_admin_role) {
    override fun render() {
        val account = member()?.takeUnless { it.locked } ?: return finish()
        memberCard(account)
        val roles = listOf("Người dùng", "Kiểm duyệt viên")
        val next = choice("Vai trò mới", roles, roles.first { it != account.roleLabel })
        card("Quyền kiểm duyệt", "Xem và xử lý báo cáo trong phạm vi được giao. Chuyển vụ việc cần xem xét thêm cho Admin.")
        val reason = input("Lý do thay đổi", "Bổ sung nhân sự kiểm duyệt cộng đồng.", multiline = true)
        button("Kiểm tra thay đổi") {
            val text = required(reason) ?: return@button
            if (next.text.toString() == account.roleLabel) {
                message("Hãy chọn vai trò khác vai trò hiện tại")
                return@button
            }
            val role = if (next.text == roles[1]) DemoRole.MODERATOR else DemoRole.LEARNER
            open(AdminConfirmRoleActivity::class.java, memberData(account).apply {
                putString(ADMIN_TARGET_ROLE, role.name)
                putString(ADMIN_REASON, text)
            })
        }
    }
}
