package com.ute.co_study.feature.learner.ui.discovery

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityMatchFiltersBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.learner.model.MatchFilters
import com.ute.co_study.feature.learner.model.MatchLevel
import com.ute.co_study.feature.learner.model.StudyDay
import com.ute.co_study.feature.learner.model.StudyMode
import com.ute.co_study.feature.learner.model.WeeklyTimeSlot

class MatchFiltersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMatchFiltersBinding
    private lateinit var draft: MatchFilters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        draft = savedInstanceState?.getBundle(DRAFT)?.toMatchFilters()
            ?: MatchFilterContract.readFilters(intent)
            ?: defaultMatchFilters()

        binding = ActivityMatchFiltersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

        binding.skillInput.setText(draft.skill)
        renderSelections()
        binding.backButton.setOnClickListener { finish() }
        binding.cancelButton.setOnClickListener { finish() }
        binding.applyButton.setOnClickListener {
            setResult(RESULT_OK, MatchFilterContract.result(currentDraft()))
            finish()
        }
        binding.levelButton.setOnClickListener {
            choose(R.string.match_filter_level, levelLabels(), draft.level.ordinal) { selected ->
                draft = draft.copy(level = MatchLevel.entries[selected])
                renderSelections()
            }
        }
        binding.modeButton.setOnClickListener {
            choose(R.string.match_filter_mode, modeLabels(), draft.mode.ordinal) { selected ->
                draft = draft.copy(mode = StudyMode.entries[selected])
                renderSelections()
            }
        }
        binding.timeButton.setOnClickListener { chooseDay() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBundle(DRAFT, currentDraft().toBundle())
        super.onSaveInstanceState(outState)
    }

    private fun currentDraft(): MatchFilters =
        draft.copy(skill = binding.skillInput.text.toString().trim())

    private fun renderSelections() {
        binding.levelButton.text = levelLabel(draft.level)
        binding.modeButton.text = modeLabel(draft.mode)
        binding.timeButton.text = draft.timeSlot?.let { timeSlotLabel(it) }
            ?: getString(R.string.match_filter_any)
    }

    private fun choose(title: Int, options: Array<String>, selected: Int, onSelected: (Int) -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setSingleChoiceItems(options, selected) { dialog, index ->
                dialog.dismiss()
                onSelected(index)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun chooseDay() {
        val options = arrayOf(getString(R.string.match_filter_any), *dayLabels())
        val current = draft.timeSlot
        choose(R.string.match_filter_time, options, (current?.day?.ordinal ?: -1) + 1) { selected ->
            if (selected == 0) {
                draft = draft.copy(timeSlot = null)
                renderSelections()
            } else {
                val day = StudyDay.entries[selected - 1]
                pickTime(R.string.match_filter_start, current?.startMinute ?: 19 * 60) { start ->
                    val initialEnd = current?.endMinute?.takeIf { it > start }
                        ?: (start + 60).coerceAtMost(1439)
                    chooseEndTime(day, start, initialEnd)
                }
            }
        }
    }

    private fun chooseEndTime(day: StudyDay, start: Int, initial: Int) {
        pickTime(R.string.match_filter_end, initial) { end ->
            if (end > start) {
                draft = draft.copy(timeSlot = WeeklyTimeSlot(day, start, end))
                renderSelections()
            } else {
                AlertDialog.Builder(this)
                    .setMessage(R.string.match_filter_invalid_time)
                    .setPositiveButton(R.string.match_filter_choose_again) { _, _ ->
                        chooseEndTime(day, start, initial)
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
            }
        }
    }

    private fun pickTime(title: Int, initial: Int, onSelected: (Int) -> Unit) {
        TimePickerDialog(this, { _, hour, minute -> onSelected(hour * 60 + minute) },
            initial / 60, initial % 60, true
        ).apply { setTitle(title) }.show()
    }

    private companion object {
        const val DRAFT = "filter_draft"
    }
}
