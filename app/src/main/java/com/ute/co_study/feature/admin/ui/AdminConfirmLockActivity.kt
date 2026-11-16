package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R

class AdminConfirmLockActivity : AdminActivity(R.layout.activity_admin_confirm_lock) {
    override fun render() {
        val account = member()?.takeUnless { it.locked } ?: return finish()
        val days = intent.getIntExtra(ADMIN_DAYS, 0)
        val reason = intent.getStringExtra(ADMIN_REASON) ?: return finish()
        val report = intent.getStringExtra(ADMIN_REPORT)
        card("${account.name} · ${account.id}", "Thời hạn: $days ngày\nLý do: $reason")
        if (report != null) info("Căn cứ báo cáo", report)
        note("Quyết định, lý do, người thực hiện và thời điểm sẽ được lưu vào nhật ký.", Tone.DANGER)
        button("Xác nhận khóa $days ngày", Tone.DANGER) {
            if (!admin.lock(account.id, days, reason, report)) {
                actionFailed()
                return@button
            }
            open(AdminLockedActivity::class.java, memberData(account).apply { putInt(ADMIN_DAYS, days) })
            finish()
        }
        button("Hủy thao tác", Tone.PLAIN) { finish() }
    }
}
