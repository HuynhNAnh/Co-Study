package com.ute.co_study.feature.moderation.ui.skill

import android.os.Bundle
import androidx.activity.viewModels
import com.ute.co_study.R
import com.ute.co_study.feature.moderation.viewmodel.ModerationViewModel
import com.ute.co_study.shared.data.DemoRole
import com.ute.co_study.shared.model.SkillEvidenceStatus
import com.ute.co_study.shared.ui.WorkflowActivity

class SkillEvidenceQueueActivity : WorkflowActivity(R.layout.activity_skill_evidence_queue, DemoRole.MODERATOR) {
    private val model: ModerationViewModel by viewModels()
    override val refreshOnReturn = true

    override fun render() {
        val items = model.skillEvidence.queue()
        val (pending, reviewed) = items.partition { it.status == SkillEvidenceStatus.PENDING }
        status("${pending.size} minh chứng chờ duyệt")
        if (items.isEmpty()) note("Chưa có minh chứng kỹ năng được gửi.")

        listOf("Chờ duyệt" to pending, "Đã xử lý" to reviewed).forEach { (heading, entries) ->
            if (entries.isNotEmpty()) label(heading)
            entries.forEach { item ->
                row(item.skillSummary, "${item.ownerName} · ${item.status.label}") {
                    open(SkillEvidenceReviewActivity::class.java, Bundle().apply { putString(EXTRA_EVIDENCE, item.id) })
                }
            }
        }
    }
}

internal const val EXTRA_EVIDENCE = "skill_evidence_id"
