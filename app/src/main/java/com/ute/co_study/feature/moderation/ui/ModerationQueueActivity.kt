package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R

class ModerationQueueActivity : ModerationActivity(R.layout.activity_moderation_queue) {
    override val refreshOnReturn = true

    override fun render() {
        val items = moderation.filtered(session.name)
        status("${items.size} báo cáo · Dữ liệu trên thiết bị")
        row("Tìm kiếm & bộ lọc", "Mã báo cáo · Bằng chứng · Trạng thái") { open(ModerationFiltersActivity::class.java) }
        items.forEach { reportRow(it) }
        if (items.isEmpty()) note("Không tìm thấy báo cáo phù hợp. Bạn có thể thay đổi bộ lọc.")
        moderationTabs(1)
    }
}
