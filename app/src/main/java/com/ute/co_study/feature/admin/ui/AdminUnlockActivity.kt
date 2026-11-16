package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R

class AdminUnlockActivity : AdminActivity(R.layout.activity_admin_unlock) {
    override fun render() {
        val account = member()?.takeIf { it.locked } ?: return finish()
        status("${account.name} · ${account.id}")
        val reason = input("Lý do mở khóa", hint = "Tóm tắt căn cứ mở khóa", multiline = true)
        card("Trước khi mở khóa", "Đang tạm khóa đến ${formattedDate(account.lockedUntil)}. Khi xác nhận, tài khoản hoạt động trở lại.")
        note("Lý do khóa và quyết định mở khóa được lưu trong nhật ký.")
        button("Xác nhận mở khóa") {
            val text = required(reason) ?: return@button
            if (admin.unlock(account.id, text)) {
                openMember(AdminUnlockedActivity::class.java, account, null)
                finish()
            } else actionFailed()
        }
        button("Giữ nguyên khóa", Tone.PLAIN) { finish() }
    }
}
