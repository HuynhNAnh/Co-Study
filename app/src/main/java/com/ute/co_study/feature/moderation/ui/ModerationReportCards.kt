package com.ute.co_study.feature.moderation.ui

import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import com.ute.co_study.databinding.ItemModerationReportBinding
import com.ute.co_study.shared.model.CommunityReport

internal fun LinearLayout.showReportCards(
    reports: List<CommunityReport>,
    title: (CommunityReport) -> String,
    summary: (CommunityReport) -> String,
    open: (CommunityReport) -> Unit
) {
    removeAllViews()
    val inflater = LayoutInflater.from(context)
    reports.forEach { report ->
        val item = ItemModerationReportBinding.inflate(inflater, this, false)
        item.reportStatus.text = "${report.id} · ${report.status.label}"
        item.reportTitle.text = title(report)
        item.reportSummary.text = summary(report)
        item.root.setOnClickListener { open(report) }
        ViewCompat.setScreenReaderFocusable(item.root, true)
        addView(item.root)
    }
}

internal fun CommunityReport.subjectInitials() =
    subject.split(' ').filter(String::isNotBlank).takeLast(2)
        .joinToString("") { it.take(1) }.uppercase()
