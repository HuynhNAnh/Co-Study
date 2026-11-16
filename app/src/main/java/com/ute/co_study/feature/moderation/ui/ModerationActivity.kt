package com.ute.co_study.feature.moderation.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.viewbinding.ViewBinding
import com.ute.co_study.R
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.moderation.ui.history.HistoryDetailActivity
import com.ute.co_study.feature.moderation.ui.report.ReportDetailActivity
import com.ute.co_study.feature.moderation.ui.report.ReportSpamDetailActivity
import com.ute.co_study.feature.moderation.viewmodel.ModerationViewModel
import com.ute.co_study.shared.data.DemoRole
import com.ute.co_study.shared.data.DemoSession
import com.ute.co_study.shared.model.CommunityReport
import com.ute.co_study.shared.model.ReportAction
import com.ute.co_study.shared.model.ReportStatus

abstract class ModerationActivity<B : ViewBinding>(
    private val inflate: (LayoutInflater) -> B
) : AppCompatActivity() {
    private val model: ModerationViewModel by viewModels()
    internal val moderation get() = model
    internal val reports get() = model.reports
    internal val session by lazy { DemoSession(this) }

    protected lateinit var binding: B
        private set

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (session.role != DemoRole.MODERATOR) {
            finish()
            return
        }
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, findViewById(R.id.status_bar_space))
        ViewCompat.setAccessibilityHeading(findViewById(R.id.screen_title), true)
        findViewById<View>(R.id.back_button)?.setOnClickListener { finish() }
        bind()
    }

    final override fun onResume() {
        super.onResume()
        if (session.role != DemoRole.MODERATOR) finish()
        else if (::binding.isInitialized && !isFinishing) refresh()
    }

    protected abstract fun bind()
    protected open fun refresh() = Unit

    protected fun open(target: Class<out AppCompatActivity>) {
        startActivity(Intent(this, target))
    }

    protected fun returnTo(target: Class<out AppCompatActivity>) {
        startActivity(Intent(this, target).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
    }

    internal fun currentReport(vararg statuses: ReportStatus): CommunityReport? {
        val id = intent.getStringExtra(REPORT_ID)
        val item = reports.queue().firstOrNull { it.id == id }
        if (item != null && (statuses.isEmpty() || item.status in statuses)) return item
        message(R.string.mod_report_changed)
        finish()
        return null
    }

    internal fun describe(item: CommunityReport) {
        findViewById<TextView>(R.id.screen_subtitle).text = getString(R.string.mod_report_status, item.id, item.status.label)
    }

    internal fun openReport(target: Class<out AppCompatActivity>, item: CommunityReport, extras: Bundle? = null) {
        val navigation = Intent(this, target)
        if (extras != null) navigation.putExtras(extras)
        navigation.putExtra(REPORT_ID, item.id)
        startActivity(navigation)
    }

    internal fun showReport(item: CommunityReport) {
        val target = when {
            item.status in closedStatuses -> HistoryDetailActivity::class.java
            item.status == ReportStatus.IN_REVIEW && item.hasEvidence -> ReportSpamDetailActivity::class.java
            else -> ReportDetailActivity::class.java
        }
        openReport(target, item)
    }

    internal fun updateReport(
        item: CommunityReport,
        action: ReportAction,
        reason: String,
        destination: Class<out AppCompatActivity>
    ) {
        val updated = reports.apply(item.id, action, reason)
        if (updated == null) {
            message(R.string.mod_report_update_failed)
            return
        }
        openReport(destination, updated)
        finish()
    }

    protected fun required(field: EditText): String? {
        val value = field.text.toString().trim()
        if (value.isNotEmpty()) return value
        field.error = getString(R.string.workflow_required)
        field.requestFocus()
        return null
    }

    protected fun message(text: Int) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

    private companion object {
        const val REPORT_ID = "moderation_report_id"
        val closedStatuses = setOf(ReportStatus.RESOLVED, ReportStatus.DISMISSED, ReportStatus.ESCALATED)
    }
}
