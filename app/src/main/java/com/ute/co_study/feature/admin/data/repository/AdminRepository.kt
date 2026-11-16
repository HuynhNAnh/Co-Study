package com.ute.co_study.feature.admin.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.ute.co_study.feature.admin.model.AdminEvent
import com.ute.co_study.shared.model.ManagedAccount
import com.ute.co_study.shared.model.ManagedSkill
import com.ute.co_study.shared.data.CommunityRulesRepository
import com.ute.co_study.shared.data.DemoMemberDirectory
import com.ute.co_study.shared.data.DemoRole
import com.ute.co_study.shared.data.DemoSession
import com.ute.co_study.shared.data.ReportRepository
import com.ute.co_study.shared.data.SkillCatalog
import com.ute.co_study.shared.data.communityPreferences
import com.ute.co_study.shared.model.CommunityRules
import com.ute.co_study.shared.model.ReportAction
import com.ute.co_study.shared.model.ReportStatus
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

internal class AdminRepository(context: Context) {
    private val store = communityPreferences(context)
    private val session = DemoSession(context)
    private val directory = DemoMemberDirectory(context)
    private val rules = CommunityRulesRepository(context)
    private val skillCatalog = SkillCatalog(context)
    val reports = ReportRepository(context)

    fun accounts(): List<ManagedAccount> = if (isAdmin()) directory.all() else emptyList()
    fun account(id: String) = accounts().firstOrNull { it.id == id }

    fun lock(id: String, days: Int, reason: String, reportId: String?): Boolean {
        val current = account(id)?.takeUnless { it.locked } ?: return false
        if (days !in 1..90 || reason.isBlank()) return false
        val updated = current.copy(
            lockedUntil = System.currentTimeMillis() + days * 86_400_000L,
            lockReason = reason
        )
        val detail = "${current.name} · $id\n$days ngày · $reason" +
            reportId?.let { "\nBáo cáo: $it" }.orEmpty()
        val operation: (SharedPreferences.Editor) -> Unit = { editor ->
            directory.writeTo(editor, updated)
            writeEvent(editor, "Khóa tài khoản", detail)
        }
        if (reportId == null) return persist(operation)
        val report = reports.queue().firstOrNull {
            it.id == reportId && it.status == ReportStatus.ESCALATED && it.subject == current.name
        } ?: return false
        return reports.apply(report.id, ReportAction.LOCK_ACCOUNT, detail, writeAlongside = operation) != null
    }

    fun unlock(id: String, reason: String): Boolean {
        val current = account(id)?.takeIf { it.locked } ?: return false
        if (reason.isBlank()) return false
        return persist { editor ->
            directory.writeTo(editor, current.copy(lockedUntil = 0L, lockReason = ""))
            writeEvent(editor, "Mở khóa tài khoản", "${current.name} · $id\n$reason")
        }
    }

    fun changeRole(id: String, role: DemoRole, reason: String): Boolean {
        val current = account(id) ?: return false
        if (current.locked || role == current.role || role == DemoRole.ADMIN || reason.isBlank()) return false
        val updated = current.copy(role = role)
        return persist { editor ->
            directory.writeTo(editor, updated)
            writeEvent(editor, "Cập nhật phân quyền", "${current.name} · $id\n${current.roleLabel} → ${updated.roleLabel}\n$reason")
        }
    }

    fun skills(): List<ManagedSkill> = if (isAdmin()) skillCatalog.load() else emptyList()

    fun saveSkill(skill: ManagedSkill): Boolean {
        val existing = skills()
        if (existing.none { it.id == skill.id } || skill.name.isBlank() || skill.group.isBlank() || skill.description.isBlank()) return false
        val updated = existing.map { if (it.id == skill.id) skill else it }
        return persist { editor ->
            skillCatalog.writeTo(editor, updated)
            writeEvent(editor, "Cập nhật kỹ năng", "${skill.id} · ${skill.name}\n${skill.group} · ${skill.status}\n${skill.description}")
        }
    }

    fun config(): CommunityRules = rules.load()

    fun saveConfig(config: CommunityRules, reason: String): Boolean {
        val previous = rules.load()
        if (!isAdmin() || !config.valid || reason.isBlank() || config == previous) return false
        return persist { editor ->
            rules.writeTo(editor, config)
            writeEvent(editor, "Lưu cấu hình cộng đồng",
                "Lời mời/ngày: ${previous.dailyInvites} → ${config.dailyInvites}\n" +
                    "Hạn báo cáo: ${previous.reportHours} → ${config.reportHours} giờ\n" +
                    "Buổi hợp lệ: ${config.studyMinutes} phút · Xác nhận trong ${config.studyConfirmHours} giờ\n" +
                    "Huy hiệu: ${config.badgeDays.joinToString("/")} ngày · ${config.partnerSessions} buổi cùng bạn\n$reason")
        }
    }

    fun returnReport(id: String, reason: String): Boolean =
        reports.apply(id, ReportAction.RETURN, reason) { editor ->
            writeEvent(editor, "Trả báo cáo để bổ sung", "$id\n$reason")
        } != null

    fun resolveReport(id: String, result: String): Boolean =
        reports.apply(id, ReportAction.RESOLVE_ESCALATION, result) { editor ->
            writeEvent(editor, "Trả kết quả báo cáo", "$id\n$result")
        } != null

    fun journal(): List<AdminEvent> {
        if (!isAdmin()) return emptyList()
        val events = JSONArray(store.getString(JOURNAL, "[]") ?: "[]")
        return List(events.length()) { index ->
            val row = events.getJSONObject(index)
            AdminEvent(row.getString("title"), row.getString("actor"), row.getString("at"), row.getString("detail"))
        }.asReversed() + sampleJournal
    }

    private fun writeEvent(editor: SharedPreferences.Editor, title: String, detail: String) {
        val formatter = SimpleDateFormat("dd/MM/yyyy · HH:mm", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh")
        }
        val events = JSONArray(store.getString(JOURNAL, "[]") ?: "[]")
            .put(JSONObject().put("title", title).put("actor", session.name)
                .put("at", formatter.format(Date())).put("detail", detail))
        editor.putString(JOURNAL, events.toString())
    }

    private fun persist(write: (SharedPreferences.Editor) -> Unit): Boolean {
        if (!isAdmin()) return false
        return store.edit().also(write).commit()
    }

    private fun isAdmin() = session.role == DemoRole.ADMIN

    private companion object {
        const val JOURNAL = "admin_journal"
        val sampleJournal = listOf(
            AdminEvent("Cập nhật kỹ năng SK-006", "Admin-01", "05/10/2026 · 09:20", "Đổi nhóm: Khác → Ngôn ngữ. Chuẩn hóa danh mục."),
            AdminEvent("Mở khóa TV-011", "Admin-01", "04/10/2026 · 15:40", "Đã xác minh và khắc phục nội dung vi phạm."),
            AdminEvent("Cấp quyền KDV-01", "Admin-02", "03/10/2026 · 10:05", "Bổ sung nhân sự kiểm duyệt.")
        )
    }
}
