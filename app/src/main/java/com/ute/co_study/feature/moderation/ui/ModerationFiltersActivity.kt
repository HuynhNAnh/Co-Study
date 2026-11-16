package com.ute.co_study.feature.moderation.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus

class ModerationFiltersActivity : ModerationActivity(R.layout.activity_moderation_filters) {
    override fun render() {
        val query = input("Mã hoặc tên báo cáo", moderation.query, "Tất cả báo cáo", maxLength = 100)
        val evidence = choice("Bằng chứng", listOf("Tất cả", "Có bằng chứng", "Thiếu bằng chứng"), moderation.subjectType)
        val states = listOf("Tất cả") + ReportStatus.entries.map { it.label }
        val status = choice("Trạng thái", states, moderation.status?.label ?: states.first())
        val owners = listOf("Tất cả báo cáo", "Báo cáo tôi tiếp nhận")
        val owner = choice("Người phụ trách", owners, owners[if (moderation.onlyAssigned) 1 else 0])
        button("Áp dụng bộ lọc") {
            val selection = ReportStatus.entries.firstOrNull { it.label == status.text.toString() }
            moderation.saveFilters(query.text.toString(), evidence.text.toString(), selection, owner.text == owners[1])
            finish()
        }
        button("Xóa bộ lọc", Tone.PLAIN) {
            moderation.saveFilters("", "", null, false)
            refresh()
        }
    }
}
