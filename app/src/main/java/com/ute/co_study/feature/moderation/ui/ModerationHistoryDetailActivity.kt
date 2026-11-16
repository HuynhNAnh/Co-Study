package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R

class ModerationHistoryDetailActivity : ModerationActivity(R.layout.activity_moderation_history_detail) {
    override fun render() {
        val item = report() ?: return
        describe(item)
        info("Nội dung được xử lý", "${item.subject} · ${item.title}")
        info("Căn cứ đã lưu", item.evidence.ifBlank { item.detail })
        item.history.asReversed().forEach { event ->
            card(event.action.label, "${event.actor} · ${event.at}\n${event.reason}".trimEnd())
        }
        if (item.history.isEmpty()) note("Báo cáo chưa có thao tác xử lý.")
        button("Về lịch sử xử lý", Tone.PLAIN) { returnTo(ModerationHistoryActivity::class.java) }
    }
}
