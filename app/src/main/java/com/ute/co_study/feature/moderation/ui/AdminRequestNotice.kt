package com.ute.co_study.feature.moderation.ui

import androidx.core.view.isVisible
import com.ute.co_study.databinding.ViewModerationAdminRequestBinding
import com.ute.co_study.shared.model.CommunityReport

internal fun ViewModerationAdminRequestBinding.showAdminRequest(report: CommunityReport) {
    val request = report.pendingAdminRequest
    root.isVisible = request != null
    requestDetails.text = request?.let { "${it.at}\n${it.reason}" }.orEmpty()
}
