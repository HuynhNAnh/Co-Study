package com.ute.co_study.feature.admin.model

/** Sample content for the escalation workflow. */
object AdminEscalationScreens {
    fun all(): Map<Int, AdminScreen> = mapOf(
12 to AdminScreen("Báo cáo chuyển cấp", "02 vụ việc chờ Admin xem xét", listOf(
            AdminBlock("CS-202610-002", "Thành Đạt · Nội dung quảng cáo lặp lại\nĐã chuyển cấp · Chờ quyết định", 4),
            AdminBlock("CS-202610-003", "Hồ sơ tái phạm · Còn thiếu bằng chứng\nĐã chuyển cấp · Cần xem xét", 4),
            AdminBlock("Phạm vi Admin", "Admin xử lý quyết định ở cấp tài khoản; nội dung báo cáo thông thường thuộc quy trình kiểm duyệt.", 1)
        )),
13 to AdminScreen("Xem xét báo cáo", "CS-202610-002 · Chờ quyết định", listOf(
            AdminBlock("Thành Đạt · ND-002", "Người báo cáo: Thu Hà.\nNội dung quảng cáo lặp lại 03 lần.", 1),
            AdminBlock("Bằng chứng", "“Đăng ký khóa học ngay! Nhắn tin nhận ưu đãi.” Trích nội dung giới thiệu ND-002.", 0),
            AdminBlock("Kiểm duyệt viên đề xuất", "KDV-01: kiểm tra lịch sử tái phạm và xem xét biện pháp ở cấp tài khoản.", 0),
            AdminBlock("Xem lịch sử tài khoản", "02 cảnh báo trước đó · TV-024", 4)
        ), listOf(AdminAction("Xem xét khóa tài khoản", 4, danger = true), AdminAction("Yêu cầu bổ sung", 14, secondary = true)), back = 12),
14 to AdminScreen("Yêu cầu bổ sung", "Chuyển lại cho KDV-01", listOf(
            AdminBlock("Trạng thái sau khi gửi", "CS-202610-002 chuyển sang “Chờ KDV bổ sung”. Chưa áp dụng chế tài.", 1),
            AdminBlock("Hướng dẫn xử lý", "Nêu rõ thông tin hoặc bằng chứng cần bổ sung để người nhận có thể tiếp tục.", 0)
        ), listOf(AdminAction("Xác nhận chuyển lại", 15), AdminAction("Hủy yêu cầu", 13, secondary = true)), "Nhập nội dung yêu cầu bổ sung", 13),
15 to AdminScreen("Đã chuyển yêu cầu", "CS-202610-002 · Demo", listOf(
            AdminBlock("Chờ kiểm duyệt bổ sung", "Chưa áp dụng chế tài.", 1),
            AdminBlock("Thành Đạt vẫn đang hoạt động", "Admin xem xét lại khi hồ sơ được gửi lên lần nữa.", 0)
        ), listOf(AdminAction("Về tổng quan", 1)), back = 12),
35 to AdminScreen("Hồ sơ tái phạm", "CS-202610-003 · Báo cáo chuyển cấp", listOf(
            AdminBlock("ĐÃ CHUYỂN CẤP", "Hồ sơ cần Admin xem xét do có lịch sử vi phạm trước đó.", 1),
            AdminBlock("BẰNG CHỨNG CÒN THIẾU", "Kiểm duyệt viên cần bổ sung bằng chứng về lần vi phạm gần nhất trước khi Admin quyết định.", 0)
        ), listOf(AdminAction("Xem yêu cầu bổ sung", 36), AdminAction("Về danh sách", 12, secondary = true)), back = 12),
36 to AdminScreen("Yêu cầu bổ sung", "CS-202610-003 · Gửi lại kiểm duyệt", listOf(
            AdminBlock("NỘI DUNG YÊU CẦU", "Nêu rõ bằng chứng và mốc thời gian còn thiếu.", 0),
            AdminBlock("SAU KHI GỬI", "Hồ sơ chuyển sang Chờ bổ sung và quay lại hàng đợi của kiểm duyệt viên.", 1)
        ), listOf(AdminAction("Gửi yêu cầu bổ sung", 37), AdminAction("Hủy", 35, secondary = true)), "Nhập nội dung yêu cầu", 35),
37 to AdminScreen("Đã gửi lại kiểm duyệt", "CS-202610-003 · Chờ bổ sung", listOf(
            AdminBlock("CẦN BỔ SUNG", "Yêu cầu đã được gửi đến kiểm duyệt viên phụ trách.", 1),
            AdminBlock("Tiếp theo", "Admin có thể xem xét lại hồ sơ sau khi bằng chứng được bổ sung.", 0)
        ), listOf(AdminAction("Về tổng quan", 1)), back = 12)
    )
}
