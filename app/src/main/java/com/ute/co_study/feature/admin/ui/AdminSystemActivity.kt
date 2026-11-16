package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R

class AdminSystemActivity : AdminActivity(R.layout.activity_admin_system) {
    override fun render() {
        row("Cấu hình cộng đồng", "Xem và xác nhận thay đổi tham số") { open(AdminConfigActivity::class.java) }
        row("Nhật ký quản trị", "Tra cứu ai đã thay đổi và vì sao") { open(AdminJournalActivity::class.java) }
        row("Hồ sơ Admin", "Thông tin tài khoản và quyền hạn") { open(AdminProfileActivity::class.java) }
        note("Các mục trong khu vực quản trị dùng dữ liệu trình diễn trên thiết bị.")
        adminTabs(4)
    }
}
