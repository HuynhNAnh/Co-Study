package com.ute.co_study.feature.learner.ui.profile

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.matchesLearnerSearch

internal class SkillPicker(
    private val input: MaterialAutoCompleteTextView,
    private val chips: ChipGroup,
    private val suggestions: List<String>,
    initialSkills: List<String>
) {
    private val selected = initialSkills.toMutableList()

    init {
        input.threshold = 1
        input.setOnItemClickListener { parent, _, position, _ ->
            val skill = parent.getItemAtPosition(position) as? String ?: return@setOnItemClickListener
            add(skill)
        }
        input.setOnEditorActionListener { _, action, event ->
            val enter = event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN
            if (action == EditorInfo.IME_ACTION_DONE || enter) commitTypedSkill() else false
        }
        renderSelection()
    }

    fun selectedSkills(): List<String> = selected.toList()

    fun commitTypedSkill(): Boolean = add(input.text?.toString().orEmpty())

    private fun add(value: String): Boolean {
        val skill = value.trim()
        if (skill.isEmpty()) return false
        if (selected.none { it.equals(skill, ignoreCase = true) }) selected.add(skill)
        input.error = null
        input.setText("", false)
        input.dismissDropDown()
        renderSelection()
        return true
    }

    private fun renderSelection() {
        chips.removeAllViews()
        chips.isVisible = selected.isNotEmpty()
        val inflater = LayoutInflater.from(chips.context)
        selected.forEach { skill ->
            val chip = inflater.inflate(R.layout.item_profile_skill_chip, chips, false) as Chip
            chip.text = skill
            chip.closeIconContentDescription = chips.context.getString(R.string.profile_skill_remove, skill)
            chip.setOnCloseIconClickListener {
                selected.remove(skill)
                renderSelection()
            }
            chips.addView(chip)
        }

        val available = suggestions.filter { option ->
            selected.none { it.equals(option, ignoreCase = true) }
        }
        val adapter = SkillSuggestions(input.context, available)
        input.setAdapter(adapter)
        val query = input.text?.toString().orEmpty()
        if (query.isNotBlank()) adapter.filter.filter(query)
    }
}

private class SkillSuggestions(context: Context, private val skills: List<String>) :
    ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line) {
    private var matches = skills

    override fun getCount(): Int = matches.size

    override fun getItem(position: Int): String = matches[position]

    override fun getFilter(): Filter = skillFilter

    private val skillFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val query = constraint?.toString().orEmpty()
            val results = skills.filter { it.matchesLearnerSearch(query) }
            return FilterResults().apply {
                values = results
                count = results.size
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            matches = (results?.values as? List<*>)?.filterIsInstance<String>().orEmpty()
            notifyDataSetChanged()
        }
    }
}
