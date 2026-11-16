package com.ute.co_study.feature.learner.ui.discovery

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityDiscoveryBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.learner.model.MatchFilters
import com.ute.co_study.feature.learner.model.MatchLevel
import com.ute.co_study.feature.learner.model.StudyMode
import com.ute.co_study.feature.learner.ui.markCurrentLearnerTab
import com.ute.co_study.feature.learner.ui.profile.PartnerProfileActivity
import com.ute.co_study.feature.learner.ui.showLearnerFeatureUnavailable
import com.ute.co_study.feature.learner.viewmodel.discovery.DiscoveryViewModel

class DiscoveryActivity : AppCompatActivity() {
    private val viewModel: DiscoveryViewModel by viewModels()
    private lateinit var appliedFilters: MatchFilters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appliedFilters = savedInstanceState?.getBundle(APPLIED_FILTERS)?.toMatchFilters()
            ?: defaultMatchFilters()
        val binding = ActivityDiscoveryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)
        ViewCompat.setScreenReaderFocusable(binding.filterButton, true)
        ViewCompat.setScreenReaderFocusable(binding.partnerCard, true)

        val editFilters = registerForActivityResult(MatchFilterContract()) { filters ->
            if (filters != null) {
                appliedFilters = filters
                renderResults(binding)
            }
        }
        binding.filterButton.setOnClickListener {
            WindowCompat.getInsetsController(window, binding.root).hide(WindowInsetsCompat.Type.ime())
            editFilters.launch(appliedFilters)
        }
        binding.searchInput.doAfterTextChanged { renderResults(binding) }
        renderResults(binding)
        binding.searchInput.setOnEditorActionListener { _, action, _ ->
            if (action != EditorInfo.IME_ACTION_SEARCH) {
                return@setOnEditorActionListener false
            }
            WindowCompat.getInsetsController(window, binding.root).hide(WindowInsetsCompat.Type.ime())
            true
        }

        val navigation = binding.learnerNavigation
        navigation.matchTab.markCurrentLearnerTab()
        navigation.homeTab.setOnClickListener { finish() }
        binding.partnerCard.setOnClickListener {
            WindowCompat.getInsetsController(window, binding.root).hide(WindowInsetsCompat.Type.ime())
            val profile = Intent(this, PartnerProfileActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(profile)
        }
        showLearnerFeatureUnavailable(
            navigation.messagesTab,
            navigation.scheduleTab,
            navigation.profileTab
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBundle(APPLIED_FILTERS, appliedFilters.toBundle())
        super.onSaveInstanceState(outState)
    }

    private fun renderResults(binding: ActivityDiscoveryBinding) {
        val filters = appliedFilters
        binding.filterSummary.text = listOfNotNull(
            filters.skill.takeIf { it.isNotBlank() },
            filters.level.takeIf { it != MatchLevel.ANY }?.let { levelLabel(it) },
            filters.mode.takeIf { it != StudyMode.ANY }?.let { modeLabel(it) },
            filters.timeSlot?.let { timeSlotLabel(it) }
        ).joinToString(getString(R.string.match_filter_separator))
            .ifEmpty { getString(R.string.match_filter_none) }

        val hasMatch = viewModel.matchesSample(binding.searchInput.text.toString(), filters)
        binding.partnerCard.isVisible = hasMatch
        binding.emptyResults.isVisible = !hasMatch
    }

    private companion object {
        const val APPLIED_FILTERS = "applied_filters"
    }
}
