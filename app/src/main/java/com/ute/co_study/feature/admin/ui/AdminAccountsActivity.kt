package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R

class AdminAccountsActivity : AdminActivity(R.layout.activity_admin_accounts) {
    override val refreshOnReturn = true

    override fun render() {
        label("TÀI KHOẢN CẦN XEM XÉT")
        admin.accounts().forEach { item ->
            row(item.name, "${item.id} · ${item.status} · ${item.roleLabel}") {
                openMember(AdminAccountDetailsActivity::class.java, item)
            }
        }
        val personnel = admin.accounts().firstOrNull { it.id == "LC-014" }
        if (personnel != null && !personnel.locked) {
            label("PHÂN QUYỀN NHÂN SỰ")
            row("Phân quyền ${personnel.name}", "${personnel.id} · ${personnel.roleLabel}") {
                openMember(AdminRoleActivity::class.java, personnel)
            }
        }
        note("Mở một tài khoản để xem lịch sử và các thao tác được phép.")
        adminTabs(1)
    }
}
