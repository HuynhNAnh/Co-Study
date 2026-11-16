package com.ute.co_study.shared.model

import com.ute.co_study.shared.data.DemoRole

internal data class ManagedAccount(
    val id: String,
    val name: String,
    val email: String,
    val role: DemoRole,
    val lockedUntil: Long = 0L,
    val lockReason: String = ""
) {
    val locked get() = lockedUntil > System.currentTimeMillis()
    val roleLabel get() = if (role == DemoRole.MODERATOR) "Kiểm duyệt viên" else "Người dùng"
    val status get() = if (locked) "Đang tạm khóa" else "Đang hoạt động"
}
