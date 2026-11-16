package com.ute.co_study.shared.data

import android.content.Context
import android.content.SharedPreferences
import com.ute.co_study.shared.model.CommunityReport
import com.ute.co_study.shared.model.ReportAction
import com.ute.co_study.shared.model.ReportEvent
import com.ute.co_study.shared.model.ReportStatus
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

internal class ReportRepository(context: Context) {
    private val store = communityPreferences(context)
    private val session = DemoSession(context)

    fun queue(): List<CommunityReport> = when (session.role) {
        DemoRole.MODERATOR -> reports().filter(::canModerate)
        DemoRole.ADMIN -> reports().filter { item ->
            item.history.any { it.action == ReportAction.ESCALATE }
        }
        else -> emptyList()
    }

    fun ownReports(): List<CommunityReport> =
        if (session.role != DemoRole.LEARNER) emptyList()
        else reports().filter { it.senderEmail.equals(session.email, ignoreCase = true) }

    fun submit(title: String, subject: String, detail: String): CommunityReport? {
        if (session.role != DemoRole.LEARNER || title.isBlank() || detail.isBlank()) return null
        val previous = reports()
        val nextNumber = (previous.maxOfOrNull { it.id.substringAfterLast('-').toInt() } ?: 0) + 1
        val id = "CS-${format("yyyyMM")}-${nextNumber.toString().padStart(3, '0')}"
        val item = CommunityReport(id, title, session.name.ifBlank { session.email }, session.email, subject,
            detail, "", ReportStatus.NEW, createdAt = format("dd/MM/yyyy · HH:mm"))
        if (!save(listOf(item) + previous)) return null
        return item
    }

    fun apply(
        id: String,
        action: ReportAction,
        reason: String,
        writeAlongside: ((SharedPreferences.Editor) -> Unit)? = null
    ): CommunityReport? {
        val previous = reports()
        val actor = session.role ?: return null
        val item = previous.firstOrNull { it.id == id } ?: return null
        if (reason.isBlank() && action != ReportAction.CLAIM) return null
        val nextStatus = nextStatus(item, actor, action) ?: return null
        val event = ReportEvent(action, session.name.ifBlank { session.email }, reason, format("dd/MM/yyyy · HH:mm"))
        val changed = item.copy(
            status = nextStatus,
            history = item.history + event,
            evidence = if (action == ReportAction.SUPPLY_INFO) listOf(item.evidence, reason)
                .filter(String::isNotBlank).joinToString("\n\n") else item.evidence
        )
        if (!save(previous.map { if (it.id == id) changed else it }, writeAlongside)) return null
        return changed
    }

    private fun nextStatus(item: CommunityReport, role: DemoRole, action: ReportAction): ReportStatus? {
        val status = item.status
        return when (role) {
            DemoRole.LEARNER -> ReportStatus.IN_REVIEW.takeIf {
                action == ReportAction.SUPPLY_INFO && status == ReportStatus.NEEDS_INFO &&
                    item.senderEmail.equals(session.email, ignoreCase = true)
            }
            DemoRole.MODERATOR -> if (!canModerate(item)) null else when (action) {
                ReportAction.CLAIM -> ReportStatus.IN_REVIEW.takeIf {
                    status == ReportStatus.NEW || status == ReportStatus.RETURNED
                }
                ReportAction.REQUEST_INFO -> ReportStatus.NEEDS_INFO.takeIf { status == ReportStatus.IN_REVIEW }
                ReportAction.DISMISS -> ReportStatus.DISMISSED.takeIf { status == ReportStatus.IN_REVIEW }
                ReportAction.WARN -> ReportStatus.RESOLVED.takeIf { status == ReportStatus.IN_REVIEW && item.hasEvidence }
                ReportAction.ESCALATE -> ReportStatus.ESCALATED.takeIf { status == ReportStatus.IN_REVIEW && item.hasEvidence }
                else -> null
            }
            DemoRole.ADMIN -> when (action) {
                ReportAction.RETURN -> ReportStatus.RETURNED.takeIf { status == ReportStatus.ESCALATED }
                ReportAction.LOCK_ACCOUNT -> ReportStatus.RESOLVED.takeIf { status == ReportStatus.ESCALATED }
                else -> null
            }
        }
    }

    private fun canModerate(item: CommunityReport): Boolean =
        item.assignedModerator == null || item.assignedModerator == session.name

    private fun reports(): List<CommunityReport> {
        val raw = store.getString("reports", null) ?: return samples
        val array = JSONArray(raw)
        return List(array.length()) { index ->
            val json = array.getJSONObject(index)
            val events = json.getJSONArray("events")
            CommunityReport(
                json.getString("id"), json.getString("title"), json.getString("sender"),
                json.getString("email"), json.getString("subject"), json.getString("detail"),
                json.getString("evidence"), ReportStatus.valueOf(json.getString("status")),
                List(events.length()) { row ->
                    val event = events.getJSONObject(row)
                    ReportEvent(
                        ReportAction.valueOf(event.getString("action")), event.getString("actor"),
                        event.getString("reason"), event.getString("at")
                    )
                },
                json.optString("created_at")
            )
        }
    }

    private fun save(
        reports: List<CommunityReport>,
        writeAlongside: ((SharedPreferences.Editor) -> Unit)? = null
    ): Boolean {
        val array = JSONArray()
        reports.forEach { item ->
            val events = JSONArray()
            item.history.forEach { event ->
                events.put(JSONObject().put("action", event.action.name).put("actor", event.actor)
                    .put("reason", event.reason).put("at", event.at))
            }
            array.put(JSONObject().put("id", item.id).put("title", item.title).put("sender", item.sender)
                .put("email", item.senderEmail).put("subject", item.subject).put("detail", item.detail)
                .put("evidence", item.evidence).put("status", item.status.name).put("events", events)
                .put("created_at", item.createdAt))
        }
        val editor = store.edit().putString("reports", array.toString())
        writeAlongside?.invoke(editor)
        return editor.commit()
    }

    private fun format(pattern: String) = SimpleDateFormat(pattern, Locale.US).apply {
        timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh")
    }.format(Date())

    private companion object {
        val samples = listOf(
            CommunityReport(
                "CS-202610-001", "Thông tin hồ sơ không phù hợp", "Đặng Thái Nguyên", "",
                "Minh Anh", "Thông tin kỹ năng trên hồ sơ cần được kiểm tra lại.", "",
                ReportStatus.NEW, createdAt = "06/10/2026 · 09:35"
            ),
            CommunityReport(
                "CS-202610-002", "Spam trong giới thiệu hồ sơ", "Thu Hà", "", "Thành Đạt",
                "Phần giới thiệu đã lặp lại nội dung quảng cáo 3 lần.",
                "ND-002 · Đăng ký khóa học ngay! Nhắn tin nhận ưu đãi.",
                ReportStatus.IN_REVIEW, listOf(ReportEvent(ReportAction.CLAIM, "KDV-01", "", "06/10/2026 · 09:10")),
                "06/10/2026 · 09:10"
            ),
            CommunityReport(
                "CS-202610-003", "Tái phạm nhiều lần", "Hoàng Nam", "", "Thành Đạt",
                "Các bài đăng quảng cáo tiếp tục xuất hiện sau khi cảnh báo.",
                "Ảnh chụp bài đăng lặp lại sau quyết định cảnh báo.",
                ReportStatus.ESCALATED,
                listOf(ReportEvent(ReportAction.ESCALATE, "KDV-01", "Xem xét vi phạm lặp lại", "06/10/2026 · 10:15")),
                "06/10/2026 · 10:00"
            ),
            CommunityReport(
                "CS-202609-018", "Đã cảnh báo và ẩn nội dung", "Thu Hà", "", "Thành Đạt",
                "Phần giới thiệu chứa nhiều đoạn quảng cáo trùng lặp.",
                "ND-018 · Bản chụp nội dung tại thời điểm báo cáo.",
                ReportStatus.RESOLVED,
                listOf(ReportEvent(ReportAction.WARN, "KDV-01", "Nội dung quảng cáo lặp lại", "30/09/2026 · 14:20")),
                "30/09/2026 · 13:00"
            )
        )
    }
}
