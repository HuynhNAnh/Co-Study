package com.ute.co_study.shared.model

internal data class ManagedSkill(
    val id: String,
    val name: String,
    val group: String,
    val description: String,
    val visible: Boolean
) {
    val status get() = if (visible) "Đang hiển thị" else "Đã ẩn"
}
