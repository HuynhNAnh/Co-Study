package com.ute.co_study.feature.moderation.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ute.co_study.R
import com.ute.co_study.feature.moderation.viewmodel.ModerationViewModel
import com.ute.co_study.shared.data.DemoRole
import com.ute.co_study.shared.model.CommunityReport
import com.ute.co_study.shared.model.ReportAction
import com.ute.co_study.shared.model.ReportStatus
import com.ute.co_study.shared.ui.WorkflowActivity

abstract class ModerationActivity(layout: Int) : WorkflowActivity(layout, DemoRole.MODERATOR) {
    private val viewModel: ModerationViewModel by viewModels()
    internal val moderation get() = viewModel
    internal val reports get() = viewModel.reports

    protected fun moderationTabs(current: Int) = tabs(current, listOf(
        Triple("Tổng quan", R.drawable.ic_home) { returnTo(ModerationDashboardActivity::class.java) },
        Triple("Báo cáo", R.drawable.ic_message) { returnTo(ModerationQueueActivity::class.java) },
        Triple("Lịch sử", R.drawable.ic_study_calendar) { returnTo(ModerationHistoryActivity::class.java) },
        Triple("Tài khoản", R.drawable.ic_person) { returnTo(ModeratorProfileActivity::class.java) }
    ))

    internal fun report(vararg statuses: ReportStatus): CommunityReport? {
        val id = intent.getStringExtra(REPORT_ID)
        val item = reports.queue().firstOrNull { it.id == id }
        if (item != null && (statuses.isEmpty() || item.status in statuses)) return item
        message("Báo cáo không còn ở bước này")
        finish()
        return null
    }

    internal fun openReport(target: Class<out AppCompatActivity>, item: CommunityReport) =
        open(target, Bundle().apply { putString(REPORT_ID, item.id) })

    internal fun showReport(item: CommunityReport) {
        val screen = when {
            item.status in setOf(ReportStatus.RESOLVED, ReportStatus.DISMISSED, ReportStatus.ESCALATED) ->
                ModerationHistoryDetailActivity::class.java
            item.status == ReportStatus.IN_REVIEW && item.hasEvidence -> SpamReportActivity::class.java
            else -> ModerationReportActivity::class.java
        }
        openReport(screen, item)
    }

    internal fun reportRow(item: CommunityReport) =
        row(item.title, "${item.id} · ${item.status.label}\n${item.sender} · ${item.subject}") { showReport(item) }

    internal fun describe(item: CommunityReport) =
        status("${item.id} · ${item.status.label}")

    internal fun reportPerson(item: CommunityReport) {
        val initials = item.subject.split(' ').takeLast(2).joinToString("") { it.take(1) }.uppercase()
        person(initials, item.subject, "Đối tượng được báo cáo")
    }

    internal fun applyAction(item: CommunityReport, action: ReportAction, reason: String, next: Class<out AppCompatActivity>) {
        val updated = reports.apply(item.id, action, reason)
        if (updated == null) message("Thao tác không còn phù hợp với trạng thái hiện tại")
        else {
            openReport(next, updated)
            finish()
        }
    }

    internal fun resolutionActions() {
        button("Về danh sách báo cáo") { returnTo(ModerationQueueActivity::class.java) }
        button("Về tổng quan", Tone.PLAIN) { returnTo(ModerationDashboardActivity::class.java) }
    }

    internal companion object {
        const val REPORT_ID = "moderation_report_id"
    }
}
