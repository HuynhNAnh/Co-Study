package com.ute.co_study.shared.model

data class SkillEvidenceInput(
    val description: String = "",
    val attachment: SkillEvidenceAttachment? = null
) {
    val isEmpty: Boolean get() = description.isBlank() && attachment == null
}

data class SkillEvidenceAttachment(val uri: String, val name: String, val mimeType: String)

internal enum class SkillEvidenceStatus(val label: String) {
    PENDING("Chờ duyệt"),
    APPROVED("Đã duyệt"),
    REJECTED("Từ chối duyệt")
}

internal data class SkillEvidence(
    val id: String,
    val ownerEmail: String,
    val ownerName: String,
    val skill: String,
    val input: SkillEvidenceInput,
    val status: SkillEvidenceStatus,
    val submittedAt: Long,
    val reviewer: String = "",
    val reviewNote: String = "",
    val reviewedAt: Long = 0L
)
