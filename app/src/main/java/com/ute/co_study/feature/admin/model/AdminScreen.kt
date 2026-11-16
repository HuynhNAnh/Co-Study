package com.ute.co_study.feature.admin.model

data class AdminBlock(
    val heading: String,
    val body: String,
    val kind: Int = 0
)

data class AdminAction(
    val label: String,
    val destination: Int,
    val danger: Boolean = false,
    val secondary: Boolean = false
)

data class AdminScreen(
    val title: String,
    val subtitle: String = "",
    val blocks: List<AdminBlock> = emptyList(),
    val actions: List<AdminAction> = emptyList(),
    val formHint: String? = null,
    val back: Int = 1
)
