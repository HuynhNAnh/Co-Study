package com.ute.co_study.feature.admin.model

/** Sample content for the system workflow. */
object AdminSystemScreens {
    fun all(): Map<Int, AdminScreen> = mapOf(
19 to AdminScreen("Hệ thống", "Công cụ quản trị", listOf(
            AdminBlock("Cấu hình cộng đồng", "Xem và xác nhận thay đổi tham số", 4),
            AdminBlock("Chuỗi học tập và huy hiệu", "Chính sách ghi nhận, quy tắc cấp huy hiệu", 4),
            AdminBlock("Hồ sơ lượt học chuyển cấp", "Xem hồ sơ và quyết định ghi nhận", 4),
            AdminBlock("Nhật ký quản trị", "Ai đã thay đổi gì và vì sao?", 4),
            AdminBlock("Hồ sơ Admin", "Thông tin tài khoản và quyền hạn", 4)
        )),
20 to AdminScreen("Cấu hình cộng đồng", "Kiểm tra và xác nhận thay đổi", listOf(
            AdminBlock("Phạm vi áp dụng", "Giới hạn mới áp dụng cho các lời mời kết nối được tạo sau khi lưu.", 1),
            AdminBlock("Lưu ý", "Thay đổi có hiệu lực toàn hệ thống và được lưu vào nhật ký quản trị.", 0)
        ), listOf(AdminAction("Xác nhận lưu cấu hình", 21), AdminAction("Hủy thay đổi", 19, secondary = true)), "Giới hạn lời mời mỗi ngày", 19),
21 to AdminScreen("Đã lưu cấu hình", "Thay đổi hoàn tất · Demo", listOf(
            AdminBlock("Đã cập nhật giới hạn", "Mỗi thành viên được gửi tối đa 10 lời mời mỗi ngày.", 1),
            AdminBlock("Thông tin ghi nhận", "Admin-01 · Giới hạn cũ: 05; mới: 10.\nLý do đã được lưu trong nhật ký.", 0)
        ), listOf(AdminAction("Về tổng quan", 1)), back = 19),
22 to AdminScreen("Nhật ký quản trị", "Chỉ đọc · Lịch sử minh họa", listOf(
            AdminBlock("05/10/2026 · 09:20", "Admin-01 • Cập nhật SK-006.\nĐổi nhóm: Kỹ năng mềm → Lập trình.", 0),
            AdminBlock("04/10/2026 · 15:40", "Admin-01 • Mở khóa TV-011.\nLý do: đã xác minh danh tính.", 0),
            AdminBlock("03/10/2026 · 10:05", "Admin-02 • Cấp quyền KDV-01.\nNgười dùng → Kiểm duyệt viên.", 0)
        ), listOf(AdminAction("Về hệ thống", 19)), back = 19),
23 to AdminScreen("Hồ sơ quản trị", "Admin-01", listOf(
            AdminBlock("Tài khoản", "Admin-01 · admin@costudy.vn\nVai trò · Admin", 4),
            AdminBlock("Quyền quản trị", "• Quản lý tài khoản và phân quyền\n• Xử lý báo cáo chuyển cấp\n• Cập nhật danh mục và cấu hình", 1),
            AdminBlock("Nhật ký quản trị", "Kiểm tra các thay đổi đã thực hiện", 4)
        ), listOf(AdminAction("Đăng xuất", 24, danger = true)), back = 1),
24 to AdminScreen("Đăng xuất", "Kết thúc phiên quản trị", listOf(
            AdminBlock("Bạn muốn đăng xuất?", "Phiên quản trị sẽ kết thúc. Bạn cần đăng nhập lại để tiếp tục.", 1),
            AdminBlock("CO-STUDY / 03 · ADMIN", "Đăng nhập → tổng quan → tài khoản & báo cáo → danh mục → hệ thống", 0)
        ), listOf(AdminAction("Xác nhận đăng xuất", 0, danger = true), AdminAction("Tiếp tục làm việc", 23, secondary = true)), back = 23)
    )
}
