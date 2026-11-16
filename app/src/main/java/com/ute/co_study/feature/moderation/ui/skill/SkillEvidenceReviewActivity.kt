package com.ute.co_study.feature.moderation.ui.skill

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import com.ute.co_study.R
import com.ute.co_study.feature.moderation.viewmodel.ModerationViewModel
import com.ute.co_study.shared.data.DemoRole
import com.ute.co_study.shared.model.LocalStudyDay
import com.ute.co_study.shared.model.SkillEvidenceAttachment
import com.ute.co_study.shared.model.SkillEvidenceStatus
import com.ute.co_study.shared.ui.WorkflowActivity

class SkillEvidenceReviewActivity : WorkflowActivity(R.layout.activity_skill_evidence_review, DemoRole.MODERATOR) {
    private val model: ModerationViewModel by viewModels()
    override val refreshOnReturn = true

    override fun render() {
        val id = intent.getStringExtra(EXTRA_EVIDENCE).orEmpty()
        val item = model.skillEvidence.find(id)
        if (item == null) {
            message("Minh chứng đã được cập nhật hoặc rút khỏi hàng đợi.")
            finish()
            return
        }

        status(item.status.label)
        card(item.skill, "${item.ownerName} · ${item.ownerEmail}")
        info("Ngày gửi", LocalStudyDay.format(item.submittedAt))
        if (item.input.description.isNotBlank()) {
            info("Mô tả hoặc liên kết", item.input.description).setTextIsSelectable(true)
        }
        item.input.attachment?.let { file ->
            row("Mở tệp minh chứng", file.name) { openAttachment(file) }
        }

        if (item.status == SkillEvidenceStatus.PENDING) {
            val note = input("Ghi chú gửi học viên", hint = "Nhập lý do nếu từ chối", multiline = true, maxLength = 500)
            button("Duyệt minh chứng") {
                review(id, SkillEvidenceStatus.APPROVED, note.text.toString().trim())
            }
            button("Từ chối duyệt", Tone.DANGER) {
                val reason = required(note) ?: return@button
                review(id, SkillEvidenceStatus.REJECTED, reason)
            }
        } else {
            info("Người duyệt", "${item.reviewer} · ${LocalStudyDay.format(item.reviewedAt)}")
            if (item.reviewNote.isNotBlank()) info("Ghi chú gửi học viên", item.reviewNote)
        }

        button("Về danh sách minh chứng", Tone.PLAIN) { finish() }
    }

    private fun review(id: String, status: SkillEvidenceStatus, note: String) {
        if (model.skillEvidence.review(id, status, note)) {
            message("Đã lưu kết quả duyệt")
            refresh()
        } else {
            message("Chưa lưu được. Minh chứng có thể đã được cập nhật.")
            refresh(preserveInputs = true)
        }
    }

    private fun openAttachment(file: SkillEvidenceAttachment) {
        val uri = Uri.parse(file.uri)
        if (uri.scheme != "content") {
            message(getString(R.string.mod_skill_evidence_open_failed))
            return
        }
        val viewer = Intent(Intent.ACTION_VIEW)
            .setDataAndType(uri, file.mimeType)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            startActivity(viewer)
        } catch (_: ActivityNotFoundException) {
            message(getString(R.string.mod_skill_evidence_open_failed))
        } catch (_: SecurityException) {
            message(getString(R.string.mod_skill_evidence_open_failed))
        }
    }
}
