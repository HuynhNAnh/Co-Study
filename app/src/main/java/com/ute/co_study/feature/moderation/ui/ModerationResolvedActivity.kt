package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus

class ModerationResolvedActivity : ModerationActivity(R.layout.activity_moderation_resolved) {
    override fun render() {
        val item = report(ReportStatus.RESOLVED) ?: return
        describe(item)
        val event = item.lastEvent ?: return
        result("Đã ghi nhận biện pháp", event.action.label)
        info("Người xử lý", event.actor)
        info("Nhật ký", "${event.at}\n${event.reason}")
        resolutionActions()
    }
}
