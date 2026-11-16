package com.ute.co_study.feature.admin.model

/** Sample content for the skill workflow. */
object AdminSkillScreens {
    fun all(): Map<Int, AdminScreen> = mapOf(
16 to AdminScreen("Danh mục kỹ năng", "12 kỹ năng · 03 nhóm", listOf(
            AdminBlock("LẬP TRÌNH", "Kotlin / Android\nĐang hiển thị · Chỉnh sửa kỹ năng", 4),
            AdminBlock("THIẾT KẾ & NGÔN NGỮ", "Figma · UI/UX\nTiếng Anh · Tiếng Nhật", 4),
            AdminBlock("Thông tin", "Thay đổi danh mục ảnh hưởng đến lựa chọn kỹ năng khi thành viên tạo hồ sơ.", 1)
        )),
17 to AdminScreen("Chỉnh sửa kỹ năng", "SK-001 · Kotlin / Android", listOf(AdminBlock("Trạng thái hiển thị", "Đang hiển thị", 0)), listOf(AdminAction("Lưu thay đổi", 18), AdminAction("Hủy chỉnh sửa", 16, secondary = true)), "Nhập tên kỹ năng", 16),
18 to AdminScreen("Đã lưu kỹ năng", "Danh mục được cập nhật · Demo", listOf(
            AdminBlock("Đã cập nhật SK-001", "Kotlin / Android\nNhóm: Lập trình · Đang hiển thị", 1),
            AdminBlock("Mô tả mới", "Lập trình ứng dụng Android bằng Kotlin, xây dựng giao diện và tích hợp dịch vụ.", 0)
        ), listOf(AdminAction("Về danh mục", 16)), back = 16)
    )
}
