package com.ute.co_study.feature.admin.model

internal data class ManagedSkill(
    val id: String,
    val name: String,
    val group: String,
    val description: String,
    val visible: Boolean
) {
    val status get() = if (visible) "Đang hiển thị" else "Đã ẩn"
}

internal data class AdminEvent(val title: String, val actor: String, val at: String, val detail: String)
