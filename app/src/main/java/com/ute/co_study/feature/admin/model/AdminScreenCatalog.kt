package com.ute.co_study.feature.admin.model

/** Shared entry and overview screens for the Admin prototype. */
object AdminScreenCatalog {
    fun all(): Map<Int, AdminScreen> = buildMap {
        putAll(mapOf(
0 to AdminScreen("Co-Study Admin", "Khu vực quản trị hệ thống", listOf(
            AdminBlock("Giữ cộng đồng học tập an toàn", "Quản lý thành viên, xử lý báo cáo chuyển cấp và theo dõi vận hành hệ thống."),
            AdminBlock("Tài khoản quản trị", "admin@costudy.vn", 2), AdminBlock("Mật khẩu", "••••••••", 2),
            AdminBlock("Bản demo", "Thông tin đăng nhập được điền sẵn để trình diễn luồng Admin.", 1)
        ), listOf(AdminAction("Đăng nhập quản trị", 1), AdminAction("Demo · Không thể đăng nhập", 34, secondary = true))),
1 to AdminScreen("Tổng quan", "Xin chào, Quản trị viên", listOf(
            AdminBlock("Cần Admin xem xét", "02 báo cáo đang chờ quyết định ở cấp tài khoản.", 1),
            AdminBlock("Báo cáo chuyển cấp", "Kiểm tra bằng chứng và lịch sử xử lý", 4),
            AdminBlock("Quản lý thành viên", "Khóa, mở khóa và phân quyền", 4),
            AdminBlock("Nhật ký gần đây", "Theo dõi các thay đổi quản trị", 4), AdminBlock("Hồ sơ quản trị viên", "Admin-01 · Quản trị hệ thống", 4)
        ), back = 1),
34 to AdminScreen("Không thể đăng nhập", "Admin · Tình huống mẫu", listOf(
            AdminBlock("TRUY CẬP CHƯA THÀNH CÔNG", "Thông tin đăng nhập chưa chính xác hoặc tài khoản không có quyền Admin. Kiểm tra lại thông tin rồi thử lại.", 1)
        ), listOf(AdminAction("Thử lại", 0)), back = 0)
        ))
    }
}
