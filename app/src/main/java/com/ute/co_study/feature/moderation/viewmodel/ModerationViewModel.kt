package com.ute.co_study.feature.moderation.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.ute.co_study.shared.data.ReportRepository
import com.ute.co_study.shared.model.CommunityReport
import com.ute.co_study.shared.model.ReportStatus
import com.ute.co_study.shared.model.ReportAction

internal class ModerationViewModel(application: Application) : AndroidViewModel(application) {
    val reports = ReportRepository(application)
    private val preferences = application.getSharedPreferences("moderation_filters", Context.MODE_PRIVATE)

    val query get() = preferences.getString("query", "").orEmpty()
    val subjectType get() = preferences.getString("subject_type", "").orEmpty()
    val status get() = ReportStatus.entries.find { it.name == preferences.getString("status", null) }
    val onlyAssigned get() = preferences.getBoolean("only_assigned", false)

    fun saveFilters(query: String, subjectType: String, status: ReportStatus?, onlyAssigned: Boolean) {
        preferences.edit().putString("query", query.trim()).putString("subject_type", subjectType)
            .putString("status", status?.name).putBoolean("only_assigned", onlyAssigned).apply()
    }

    fun filtered(operator: String): List<CommunityReport> = reports.queue().filter { item ->
        (query.isBlank() || item.id.contains(query, ignoreCase = true) || item.title.contains(query, ignoreCase = true)) &&
            (status == null || item.status == status) &&
            (when (subjectType) {
                "Có bằng chứng" -> item.hasEvidence
                "Thiếu bằng chứng" -> !item.hasEvidence
                else -> true
            }) &&
            (!onlyAssigned || item.history.lastOrNull { it.action == ReportAction.CLAIM }?.actor == operator)
    }
}
