package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R

class AdminLockedActivity : AdminActivity(R.layout.activity_admin_locked) {
    override val refreshOnReturn = true

    override fun render() {
        val account = member() ?: return
        val report = intent.getStringExtra(ADMIN_REPORT)
        val days = intent.getIntExtra(ADMIN_DAYS, 7)
        if (account.locked) {
            result("Tạm khóa $days ngày", "${account.name} · ${account.id}\nĐến ${formattedDate(account.lockedUntil)}")
            card("Đã lưu quyết định", "Người thực hiện: ${session.name}\nLý do: ${account.lockReason}")
            if (report != null) info("Báo cáo chuyển cấp đã xử lý", report)
            button("Xem xét mở khóa") { openMember(AdminUnlockActivity::class.java, account, null) }
        } else {
            info("Trạng thái hiện tại", account.status)
        }
        button("Về tổng quan", Tone.PLAIN) { returnTo(AdminDashboardActivity::class.java) }
    }
}
