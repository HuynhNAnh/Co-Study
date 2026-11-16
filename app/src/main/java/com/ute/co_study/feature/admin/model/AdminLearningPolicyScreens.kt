package com.ute.co_study.feature.admin.model

/** Sample content for the learningpolicy workflow. */
object AdminLearningPolicyScreens {
    fun all(): Map<Int, AdminScreen> = mapOf(
25 to AdminScreen("Chính sách ghi nhận", "Cấu hình đang áp dụng · v1", listOf(
            AdminBlock("ĐIỀU KIỆN TỐI THIỂU", "25 phút · 2 xác nhận\nÁp dụng cho buổi đã kết thúc. Một ngày chỉ cộng một lượt theo UTC+7.", 0),
            AdminBlock("48 giờ để xác nhận", "Tính từ thời điểm kết thúc buổi. Trong hạn, ghi nhận về ngày học.", 0),
            AdminBlock("Có tranh chấp", "Tạm giữ lượt học đến khi có kết quả xác minh. Mỗi thay đổi lưu lý do và phiên bản.", 0)
        ), listOf(AdminAction("Soạn quy tắc v2", 31), AdminAction("Xem quy tắc cấp huy hiệu", 26, secondary = true), AdminAction("Nhật ký quyết định", 27, secondary = true)), back = 19),
26 to AdminScreen("Quy tắc cấp huy hiệu", "Danh mục đang áp dụng · v1", listOf(
            AdminBlock("CHUỖI CÁ NHÂN", "Khởi động đều · Đạt chuỗi 3 ngày liên tiếp.\nBền bỉ 7 ngày · Đạt chuỗi 7 ngày liên tiếp.\nBền bỉ 30 ngày · Đạt chuỗi 30 ngày liên tiếp.", 0),
            AdminBlock("HỢP TÁC", "Đồng đội bền bỉ · Hoàn thành 5 buổi hợp lệ với cùng một người.", 0),
            AdminBlock("Nguyên tắc", "Mỗi huy hiệu cấp một lần. Phiên bản quy tắc được lưu cùng thời điểm cấp.", 1)
        ), listOf(AdminAction("Xem nhật ký quyết định", 27)), back = 25),
27 to AdminScreen("Nhật ký quyết định", "Theo dõi các thay đổi có thể kiểm tra lại", listOf(
            AdminBlock("CS-ST-021 · Đã chuyển cấp", "Ảnh hưởng sau quyết định: tính lại chuỗi từ các lượt hợp lệ, thu hồi huy hiệu mất điều kiện và thông báo người học.", 1),
            AdminBlock("Dấu vết kiểm tra", "Mã hồ sơ, phiên bản v1, người xử lý, thời điểm và trạng thái trước / sau.", 0)
        ), listOf(AdminAction("Về chính sách ghi nhận", 25), AdminAction("Nhật ký quản trị", 22, secondary = true)), back = 19),
28 to AdminScreen("Hồ sơ lượt học", "CS-ST-021 · Chuyển từ kiểm duyệt", listOf(
            AdminBlock("KẾT QUẢ ĐỐI CHIẾU", "Buổi học không đạt điều kiện xác nhận theo chính sách v1.", 1),
            AdminBlock("ẢNH HƯỞNG", "Không tính lượt học; chuỗi cá nhân và huy hiệu được tính lại theo lịch sử hợp lệ.", 0),
            AdminBlock("Mã hồ sơ", "CS-ST-021 · Kiểm duyệt viên đã chuyển cấp", 0)
        ), listOf(AdminAction("Xem quyết định loại lượt", 29), AdminAction("Về Quản trị hệ thống", 19, secondary = true)), back = 19),
29 to AdminScreen("Xác nhận loại lượt học", "CS-ST-021 · Kiểm tra trước khi lưu", listOf(
            AdminBlock("LÝ DO BẮT BUỘC", "Giải thích căn cứ và chính sách được áp dụng.", 1),
            AdminBlock("TRƯỚC → SAU", "Đang chờ xác minh → Không được tính\nChuỗi và huy hiệu sẽ được tính lại.", 0),
            AdminBlock("Thông báo", "Sau khi lưu, người học nhận thông báo kèm lý do và mã hồ sơ.", 0)
        ), listOf(AdminAction("Loại lượt học", 30, danger = true), AdminAction("Hủy và quay lại", 28, secondary = true)), "Nhập lý do bắt buộc", 28),
30 to AdminScreen("Đã lưu quyết định", "CS-ST-021 · Không được tính", listOf(
            AdminBlock("ĐÃ XỬ LÝ", "Quyết định đã lưu và gửi thông báo cho người học.", 1),
            AdminBlock("BẢN GHI QUYẾT ĐỊNH", "CS-ST-021 · Admin-01 · Chính sách v1\nTrạng thái trước: Chờ xác minh\nTrạng thái sau: Không được tính", 0)
        ), listOf(AdminAction("Xem nhật ký quyết định", 27), AdminAction("Về Quản trị hệ thống", 19, secondary = true)), back = 19),
31 to AdminScreen("Soạn quy tắc ghi nhận", "Phiên bản dự thảo · v2", listOf(
            AdminBlock("THỜI LƯỢNG TỐI THIỂU", "Giá trị hiện tại: 25 phút", 0),
            AdminBlock("THỜI HẠN XÁC NHẬN", "Giá trị hiện tại: 48 giờ", 0),
            AdminBlock("PHẠM VI ÁP DỤNG", "Buổi học hoàn thành và được hai bên xác nhận.", 0),
            AdminBlock("Lưu ý", "Duyệt trước khi công bố để tránh thay đổi điều kiện của lịch đã tạo.", 1)
        ), listOf(AdminAction("Xem trước khi công bố", 32), AdminAction("Hủy dự thảo", 25, secondary = true)), "Nhập lý do thay đổi quy tắc", 25),
32 to AdminScreen("Công bố quy tắc v2", "Kiểm tra các thay đổi trước khi áp dụng", listOf(
            AdminBlock("THAY ĐỔI", "Thời lượng tối thiểu và thời hạn xác nhận được cập nhật.", 1),
            AdminBlock("PHIÊN BẢN CŨ", "v1 · 25 phút · xác nhận trong 48 giờ", 0),
            AdminBlock("SẴN SÀNG CÔNG BỐ", "Quy tắc mới áp dụng theo thời điểm hiệu lực đã chọn.", 0)
        ), listOf(AdminAction("Công bố v2", 33), AdminAction("Quay lại chỉnh sửa", 31, secondary = true)), back = 31),
33 to AdminScreen("Đã lên lịch áp dụng", "Phiên bản v2 · Có hiệu lực từ 12/10", listOf(
            AdminBlock("ĐÃ CÔNG BỐ", "Phiên bản v2 đã được lưu.", 1),
            AdminBlock("QUY TẮC MỚI", "Áp dụng cho các buổi học đủ điều kiện sau thời điểm hiệu lực.", 0),
            AdminBlock("NHẬT KÝ", "Người công bố: Admin-01 · Thay đổi có thể kiểm tra lại.", 0)
        ), listOf(AdminAction("Về Quản trị hệ thống", 19)), back = 19)
    )
}
