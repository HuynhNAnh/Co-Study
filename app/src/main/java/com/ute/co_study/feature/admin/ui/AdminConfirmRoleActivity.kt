package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R
import com.ute.co_study.shared.data.DemoRole

class AdminConfirmRoleActivity : AdminActivity(R.layout.activity_admin_confirm_role) {
    override fun render() {
        val account = member() ?: return
        val role = DemoRole.entries.firstOrNull { it.name == intent.getStringExtra(ADMIN_TARGET_ROLE) } ?: return finish()
        val reason = intent.getStringExtra(ADMIN_REASON) ?: return finish()
        val roleLabel = if (role == DemoRole.MODERATOR) "Kiểm duyệt viên" else "Người dùng"
        card("${account.name} · ${account.id}", "${account.roleLabel} → $roleLabel\nLý do: $reason")
        card("Phạm vi truy cập", if (role == DemoRole.MODERATOR) "Có thể sử dụng khu vực kiểm duyệt Co-Study." else "Quyền truy cập khu vực kiểm duyệt sẽ bị thu hồi.")
        note("Thay đổi có hiệu lực khi xác nhận và được ghi vào nhật ký.")
        button("Xác nhận cập nhật quyền") {
            if (admin.changeRole(account.id, role, reason)) {
                open(AdminRoleSavedActivity::class.java, memberData(account).apply { putString(ADMIN_REASON, reason) })
                finish()
            } else actionFailed()
        }
        button("Hủy thay đổi", Tone.PLAIN) { finish() }
    }
}
