package com.ute.co_study.feature.admin.ui

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ute.co_study.R
import com.ute.co_study.shared.model.ManagedAccount
import com.ute.co_study.shared.model.ManagedSkill
import com.ute.co_study.feature.admin.viewmodel.AdminViewModel
import com.ute.co_study.shared.data.DemoRole
import com.ute.co_study.shared.model.CommunityReport
import com.ute.co_study.shared.model.ReportStatus
import com.ute.co_study.shared.ui.WorkflowActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

internal const val ADMIN_MEMBER = "admin_member"
internal const val ADMIN_REPORT = "admin_report"
internal const val ADMIN_SKILL = "admin_skill"
internal const val ADMIN_REASON = "admin_reason"
internal const val ADMIN_DAYS = "admin_days"
internal const val ADMIN_TARGET_ROLE = "admin_target_role"

abstract class AdminActivity(layout: Int) : WorkflowActivity(layout, DemoRole.ADMIN) {
    private val viewModel: AdminViewModel by viewModels()
    internal val admin get() = viewModel.repository

    protected fun adminTabs(current: Int) = tabs(current, listOf(
        Triple("Tổng quan", R.drawable.ic_home) { returnTo(AdminDashboardActivity::class.java) },
        Triple("Tài khoản", R.drawable.ic_study_partners) { returnTo(AdminAccountsActivity::class.java) },
        Triple("Báo cáo", R.drawable.ic_message) { returnTo(AdminReportsActivity::class.java) },
        Triple("Danh mục", R.drawable.ic_study_calendar) { returnTo(AdminSkillsActivity::class.java) },
        Triple("Hệ thống", R.drawable.ic_person) { returnTo(AdminSystemActivity::class.java) }
    ))

    internal fun member(): ManagedAccount? {
        val id = intent.getStringExtra(ADMIN_MEMBER).orEmpty()
        return admin.account(id) ?: run { finish(); null }
    }

    internal fun memberCard(item: ManagedAccount) {
        val initials = item.name.split(' ').takeLast(2).joinToString("") { it.take(1) }.uppercase()
        person(initials, item.name, "${item.id} · ${item.roleLabel} · ${item.status}")
    }

    internal fun memberData(item: ManagedAccount, report: String? = intent.getStringExtra(ADMIN_REPORT)) =
        Bundle().apply {
            putString(ADMIN_MEMBER, item.id)
            if (report != null) putString(ADMIN_REPORT, report)
        }

    internal fun openMember(target: Class<out AppCompatActivity>, item: ManagedAccount, report: String? = intent.getStringExtra(ADMIN_REPORT)) =
        open(target, memberData(item, report))

    internal fun report(vararg statuses: ReportStatus): CommunityReport? {
        val id = intent.getStringExtra(ADMIN_REPORT)
        val item = admin.reports.queue().firstOrNull { it.id == id }
        if (item != null && (statuses.isEmpty() || item.status in statuses)) return item
        message("Báo cáo không còn ở bước này")
        finish()
        return null
    }

    internal fun openReport(target: Class<out AppCompatActivity>, item: CommunityReport) =
        open(target, Bundle().apply { putString(ADMIN_REPORT, item.id) })

    internal fun skill(): ManagedSkill? {
        val id = intent.getStringExtra(ADMIN_SKILL)
        return admin.skills().firstOrNull { it.id == id } ?: run { finish(); null }
    }

    internal fun openSkill(target: Class<out AppCompatActivity>, item: ManagedSkill) =
        open(target, Bundle().apply { putString(ADMIN_SKILL, item.id) })

    protected fun integerInput(title: String, default: Int) =
        input(title, default.toString(), type = InputType.TYPE_CLASS_NUMBER, maxLength = 3)

    protected fun integer(field: EditText, allowed: IntRange): Int? {
        val value = field.text.toString().toIntOrNull()
        if (value != null && value in allowed) return value
        field.error = "Nhập số từ ${allowed.first} đến ${allowed.last}"
        field.requestFocus()
        return null
    }

    protected fun formattedDate(timestamp: Long): String = SimpleDateFormat("dd/MM/yyyy · HH:mm", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh")
    }.format(Date(timestamp))

    protected fun actionFailed() = message("Chưa thể lưu. Kiểm tra lại trạng thái và thông tin đã nhập.")
}
