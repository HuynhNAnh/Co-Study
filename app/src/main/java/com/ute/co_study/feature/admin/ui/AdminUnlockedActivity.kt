package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R

class AdminUnlockedActivity : AdminActivity(R.layout.activity_admin_unlocked) {
    override fun render() {
        val account = member() ?: return
        result("Tài khoản hoạt động lại", "${account.name} · ${account.id}")
        card("Đã cập nhật nhật ký", "Quyết định mở khóa và lịch sử cảnh báo được lưu.")
        button("Xem tài khoản") { openMember(AdminAccountDetailsActivity::class.java, account, null); finish() }
        button("Về tổng quan", Tone.PLAIN) { returnTo(AdminDashboardActivity::class.java) }
    }
}
