package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R

class AdminLockActivity : AdminActivity(R.layout.activity_admin_lock) {
    override fun render() {
        val account = member()?.takeUnless { it.locked } ?: return finish()
        status("${account.name} · ${account.id}")
        val duration = integerInput("Thời hạn khóa (ngày)", 7)
        val reason = input("Lý do khóa", hint = "Mô tả căn cứ áp dụng biện pháp", multiline = true)
        intent.getStringExtra(ADMIN_REPORT)?.let { info("Báo cáo liên quan", it) }
        card("Phạm vi áp dụng", "Tạm dừng quyền đăng nhập, ghép cặp và nhắn tin của tài khoản trong dữ liệu mô phỏng.")
        note("Kiểm tra nội dung ở bước xác nhận trước khi áp dụng.", Tone.DANGER)
        button("Tiếp tục xác nhận", Tone.DANGER) {
            val days = integer(duration, 1..90) ?: return@button
            val explanation = required(reason) ?: return@button
            open(AdminConfirmLockActivity::class.java, memberData(account).apply {
                putInt(ADMIN_DAYS, days)
                putString(ADMIN_REASON, explanation)
            })
        }
    }
}
