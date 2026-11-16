package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R

class AdminProfileActivity : AdminActivity(R.layout.activity_admin_profile) {
    override fun render() {
        status(session.name)
        person("AD", "Quản trị viên", session.email)
        card("Vai trò · Admin", "Quản lý tài khoản và phân quyền.\nXử lý báo cáo chuyển cấp.\nQuản lý kỹ năng, cấu hình và nhật ký.")
        row("Nhật ký quản trị", "Kiểm tra các thay đổi đã thực hiện") { open(AdminJournalActivity::class.java) }
        button("Đăng xuất", Tone.PLAIN) { open(AdminSignOutActivity::class.java) }
    }
}
