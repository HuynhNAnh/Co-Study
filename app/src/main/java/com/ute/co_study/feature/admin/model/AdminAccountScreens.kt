package com.ute.co_study.feature.admin.model

/** Sample content for the account workflow. */
object AdminAccountScreens {
    fun all(): Map<Int, AdminScreen> = mapOf(
2 to AdminScreen("Tài khoản", "128 thành viên · Dữ liệu minh họa", listOf(
            AdminBlock("TÀI KHOẢN CẦN XEM XÉT", "Thành Đạt\nTV-024 · Đang hoạt động · Người dùng", 4),
            AdminBlock("PHÂN QUYỀN NHÂN SỰ", "Lan Chi\nLC-014 · Người dùng → Kiểm duyệt viên", 4),
            AdminBlock("Gợi ý thao tác", "Chọn Thành Đạt để xem luồng khóa / mở khóa; chọn Lan Chi để xem luồng cấp quyền.", 1),
            AdminBlock("Danh sách", "Danh sách rút gọn cho kịch bản trình diễn.", 0)
        )),
3 to AdminScreen("Chi tiết tài khoản", "TV-024 · Thành Đạt", listOf(
            AdminBlock("Thành Đạt", "TV-024 · Người dùng · Đang hoạt động", 4), AdminBlock("Thống kê", "2 cảnh báo     1 chuyển cấp     15 buổi học", 3),
            AdminBlock("Lịch sử cần xem xét", "02/10 và 04/10: đã cảnh báo quảng cáo lặp lại.", 1),
            AdminBlock("CS-202610-002", "Xem bằng chứng và đề xuất của kiểm duyệt viên.", 4)
        ), listOf(AdminAction("Xem xét khóa tài khoản", 4, danger = true)), back = 2),
4 to AdminScreen("Khóa tài khoản", "Thành Đạt · TV-024", listOf(
            AdminBlock("Phạm vi áp dụng", "Tạm dừng đăng nhập, ghép cặp và nhắn tin.", 1), AdminBlock("Lưu ý", "Lý do sẽ được lưu trong nhật ký quản trị và hiển thị theo chính sách của hệ thống.", 0)
        ), listOf(AdminAction("Tiếp tục xác nhận", 5), AdminAction("Hủy", 3, secondary = true)), "Nhập lý do khóa tài khoản", 3),
5 to AdminScreen("Xác nhận khóa", "Kiểm tra trước khi áp dụng", listOf(
            AdminBlock("Thành Đạt · TV-024", "Thời hạn: 07 ngày, từ lúc xác nhận.\nLý do sẽ được ghi vào nhật ký.", 1),
            AdminBlock("Thông báo thành viên", "Thành viên sẽ nhận thông báo về quyết định này.", 0), AdminBlock("Mở khóa sớm", "Bạn có thể mở khóa sớm sau khi xem xét lại hồ sơ.", 0)
        ), listOf(AdminAction("Xác nhận khóa 7 ngày", 6, danger = true), AdminAction("Hủy thao tác", 3, secondary = true)), back = 4),
6 to AdminScreen("Đã khóa tài khoản", "Kết quả xử lý · Demo", listOf(
            AdminBlock("Tạm khóa 07 ngày", "Thành Đạt · TV-024\nQuyết định dựa trên cảnh báo và hồ sơ chuyển cấp.", 1),
            AdminBlock("Đã ghi nhận quyết định", "Người thực hiện: Admin-01\nThông báo đã được gửi đến thành viên.", 0)
        ), listOf(AdminAction("Xem xét mở khóa", 7), AdminAction("Về tổng quan", 1, secondary = true)), back = 3),
7 to AdminScreen("Mở khóa tài khoản", "Thành Đạt · TV-024", listOf(
            AdminBlock("Trước khi mở khóa", "Tài khoản hiện đang bị khóa 07 ngày. Khi mở khóa, thành viên có thể đăng nhập, ghép cặp và nhắn tin lại.", 1),
            AdminBlock("Kiểm tra hồ sơ", "Xem lại lý do khóa, lịch sử báo cáo và hoạt động gần đây trước khi xác nhận.", 0)
        ), listOf(AdminAction("Xác nhận mở khóa", 8), AdminAction("Giữ nguyên khóa", 6, secondary = true)), "Nhập lý do mở khóa", 6),
8 to AdminScreen("Đã mở khóa", "Kết quả xem xét lại · Demo", listOf(
            AdminBlock("Tài khoản hoạt động lại", "Thành Đạt · TV-024\nCó thể đăng nhập, ghép cặp và nhắn tin.", 1),
            AdminBlock("Đã cập nhật nhật ký", "Admin-01 đã mở khóa sau khi xem xét phản hồi.", 0)
        ), listOf(AdminAction("Xem tài khoản", 3), AdminAction("Về tổng quan", 1, secondary = true)), back = 3),
9 to AdminScreen("Phân quyền", "LC-014 · Lan Chi", listOf(
            AdminBlock("Thành viên", "Lan Chi · LC-014\nVai trò hiện tại: Người dùng", 4),
            AdminBlock("Quyền được cấp", "• Xem báo cáo và bằng chứng\n• Yêu cầu bổ sung thông tin\n• Gửi vụ việc lên Admin\n• Xem lịch sử xử lý", 1),
            AdminBlock("Phạm vi", "Không có quyền khóa tài khoản hoặc thay đổi cấu hình hệ thống.", 0)
        ), listOf(AdminAction("Kiểm tra thay đổi", 10), AdminAction("Hủy", 2, secondary = true)), "Nhập lý do cấp quyền", 2),
10 to AdminScreen("Xác nhận phân quyền", "Kiểm tra phạm vi được cấp", listOf(
            AdminBlock("Lan Chi · LC-014", "Người dùng → Kiểm duyệt viên\nLý do: bổ sung nhân sự kiểm duyệt.", 1),
            AdminBlock("Phạm vi truy cập", "Cho phép xử lý nội dung và báo cáo. Không cấp quyền quản trị tài khoản hoặc cấu hình.", 0)
        ), listOf(AdminAction("Xác nhận cấp quyền", 11), AdminAction("Hủy thay đổi", 9, secondary = true)), back = 9),
11 to AdminScreen("Đã cập nhật quyền", "LC-014 · Lan Chi", listOf(
            AdminBlock("Đã cấp quyền kiểm duyệt", "Lan Chi có thể truy cập khu vực kiểm duyệt viên.", 1),
            AdminBlock("Thông tin ghi nhận", "Vai trò mới: Kiểm duyệt viên\nNgười cấp: Admin-01\nThời điểm: vừa xong", 0)
        ), listOf(AdminAction("Về tổng quan", 1)), back = 2)
    )
}
