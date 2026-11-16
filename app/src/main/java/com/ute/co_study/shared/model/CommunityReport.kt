package com.ute.co_study.shared.model

internal enum class ReportStatus(val label: String) {
    NEW("Mới tiếp nhận"),
    IN_REVIEW("Đang xử lý"),
    NEEDS_INFO("Cần bổ sung"),
    ESCALATED("Đã chuyển cấp"),
    RETURNED("Chờ kiểm duyệt bổ sung"),
    RESOLVED("Đã xử lý"),
    DISMISSED("Đã đóng")
}

internal enum class ReportAction(val label: String) {
    CLAIM("Tiếp nhận báo cáo"),
    REQUEST_INFO("Yêu cầu người báo cáo bổ sung"),
    SUPPLY_INFO("Bổ sung thông tin"),
    DISMISS("Bác bỏ báo cáo"),
    WARN("Ẩn nội dung và gửi cảnh báo"),
    ESCALATE("Chuyển Admin xem xét"),
    RETURN("Chuyển kiểm duyệt bổ sung"),
    LOCK_ACCOUNT("Khóa tài khoản liên quan")
}

internal data class ReportEvent(
    val action: ReportAction,
    val actor: String,
    val reason: String,
    val at: String
)

internal data class CommunityReport(
    val id: String,
    val title: String,
    val sender: String,
    val senderEmail: String,
    val subject: String,
    val detail: String,
    val evidence: String,
    val status: ReportStatus,
    val history: List<ReportEvent> = emptyList(),
    val createdAt: String = ""
) {
    val lastEvent get() = history.lastOrNull()
    val hasEvidence get() = evidence.isNotBlank()
    val assignedModerator get() = history.firstOrNull {
        it.action == ReportAction.CLAIM || it.action == ReportAction.ESCALATE || it.action == ReportAction.WARN
    }?.actor
}
