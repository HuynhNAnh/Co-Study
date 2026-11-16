package com.ute.co_study.shared.ui

import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import com.ute.co_study.shared.data.DemoRole

abstract class StaffLoginActivity(
    layout: Int,
    private val role: DemoRole,
    private val demoEmail: String,
    private val operatorId: String,
    private val destination: Class<out AppCompatActivity>
) : WorkflowActivity(layout) {
    override fun render() {
        card("Cộng đồng học tập tích cực", "Xem xét có căn cứ. Các quyết định được lưu trong nhật ký trên thiết bị.", Tone.PRIMARY)
        val email = input("Email tài khoản được cấp", demoEmail, type = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        val password = input("Mật khẩu demo", DEMO_PASSWORD, type = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
        note("Tài khoản minh họa đã điền sẵn. Phiên này dùng dữ liệu trên thiết bị.")
        button(if (role == DemoRole.ADMIN) "Đăng nhập quản trị" else "Đăng nhập kiểm duyệt") {
            if (!email.text.toString().trim().equals(demoEmail, ignoreCase = true)) {
                email.error = "Dùng tài khoản mẫu $demoEmail"
                email.requestFocus()
                return@button
            }
            if (password.text.toString() != DEMO_PASSWORD) {
                password.error = "Mật khẩu demo là $DEMO_PASSWORD"
                password.requestFocus()
                return@button
            }
            session.start(role, demoEmail, operatorId)
            replaceTask(destination)
        }
    }

    private companion object {
        const val DEMO_PASSWORD = "costudy-demo"
    }
}
