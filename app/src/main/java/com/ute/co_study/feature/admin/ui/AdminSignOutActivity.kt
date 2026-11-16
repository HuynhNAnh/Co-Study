package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R
import com.ute.co_study.feature.role.ui.RoleSelectionActivity

class AdminSignOutActivity : AdminActivity(R.layout.activity_admin_sign_out) {
    override fun render() {
        card("Bạn muốn đăng xuất?", "Phiên quản trị sẽ kết thúc. Bạn cần đăng nhập lại để truy cập các chức năng Admin.")
        note("Các thay đổi đã xác nhận được lưu trên thiết bị.")
        button("Xác nhận đăng xuất") {
            session.end()
            replaceTask(RoleSelectionActivity::class.java)
        }
        button("Tiếp tục làm việc", Tone.PLAIN) { finish() }
    }
}
