package com.ute.co_study.feature.moderation.ui.report

import com.ute.co_study.databinding.ActivityReportFilterBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.R
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.ute.co_study.shared.model.ReportStatus

class ReportFilterActivity : ModerationActivity<ActivityReportFilterBinding>(ActivityReportFilterBinding::inflate) {
    override fun bind() {
        val evidenceChoices = listOf(
            getString(R.string.mod_filter_all), getString(R.string.mod_filter_with_evidence),
            getString(R.string.mod_filter_without_evidence)
        )
        val storedEvidence = listOf("", "Có bằng chứng", "Thiếu bằng chứng")
        val statuses = listOf(null) + ReportStatus.entries
        val owners = listOf(getString(R.string.mod_filter_all), getString(R.string.mod_filter_assigned_to_me))

        binding.queryInput.setText(moderation.query)
        bindChoices(binding.evidenceInput, evidenceChoices, storedEvidence.indexOf(moderation.subjectType))
        bindChoices(binding.statusInput, statuses.map { it?.label ?: getString(R.string.mod_filter_all) }, statuses.indexOf(moderation.status))
        bindChoices(binding.assigneeInput, owners, if (moderation.onlyAssigned) 1 else 0)

        binding.applyFilterButton.setOnClickListener {
            moderation.saveFilters(
                binding.queryInput.text.toString(),
                storedEvidence.getOrElse(binding.evidenceInput.selectedItemPosition) { "" },
                statuses.getOrNull(binding.statusInput.selectedItemPosition),
                binding.assigneeInput.selectedItemPosition == 1
            )
            finish()
        }
        binding.resetFilterButton.setOnClickListener {
            binding.queryInput.text?.clear()
            binding.evidenceInput.setSelection(0)
            binding.statusInput.setSelection(0)
            binding.assigneeInput.setSelection(0)
        }
        binding.backListButton.setOnClickListener { finish() }
    }

    private fun bindChoices(spinner: Spinner, options: List<String>, selection: Int) {
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner.setSelection(selection.coerceAtLeast(0))
    }
}
