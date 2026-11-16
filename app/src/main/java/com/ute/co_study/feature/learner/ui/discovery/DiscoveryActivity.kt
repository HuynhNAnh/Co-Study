package com.ute.co_study.feature.learner.ui.discovery

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.ute.co_study.databinding.ActivityDiscoveryBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.learner.ui.markCurrentLearnerTab
import com.ute.co_study.feature.learner.ui.showLearnerFeatureUnavailable
import com.ute.co_study.feature.learner.viewmodel.discovery.DiscoveryViewModel

class DiscoveryActivity : AppCompatActivity() {
    private val viewModel: DiscoveryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDiscoveryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)
        ViewCompat.setScreenReaderFocusable(binding.filterButton, true)
        ViewCompat.setScreenReaderFocusable(binding.partnerCard, true)

        binding.searchInput.doAfterTextChanged { query ->
            val hasMatch = viewModel.matchesSample(query.toString())
            binding.partnerCard.isVisible = hasMatch
            binding.emptyResults.isVisible = !hasMatch
        }
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
        showLearnerFeatureUnavailable(
            binding.filterButton,
            binding.partnerCard,
            navigation.messagesTab,
            navigation.scheduleTab,
            navigation.profileTab
        )
    }
}
