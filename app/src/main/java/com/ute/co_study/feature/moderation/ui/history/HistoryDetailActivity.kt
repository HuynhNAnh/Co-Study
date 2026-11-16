package com.ute.co_study.feature.moderation.ui.history

import com.ute.co_study.databinding.ActivityHistoryDetailBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.R
import androidx.core.view.isVisible

class HistoryDetailActivity : ModerationActivity<ActivityHistoryDetailBinding>(ActivityHistoryDetailBinding::inflate) {
    override fun bind() {
        binding.backToHistoryButton.setOnClickListener { finish() }
    }

    override fun refresh() {
        val item = currentReport() ?: return
        describe(item)
        binding.reportContent.text = "${item.subject} · ${item.title}\n${item.detail}"
        binding.reportConclusion.text = item.lastEvent?.action?.label ?: item.status.label
        binding.reportEvidence.text = item.evidence.ifBlank { getString(R.string.mod_no_evidence) }
        binding.reportTrail.text = item.history.asReversed().joinToString("\n\n") { event ->
            listOf(event.action.label, "${event.actor} · ${event.at}", event.reason)
                .filter(String::isNotBlank).joinToString("\n")
        }
        binding.trailCard.isVisible = item.history.isNotEmpty()
    }
}
