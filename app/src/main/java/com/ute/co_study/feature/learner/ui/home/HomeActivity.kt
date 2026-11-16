package com.ute.co_study.feature.learner.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityHomeBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.learner.model.ProfileSetupDraft
import com.ute.co_study.feature.learner.ui.discovery.DiscoveryActivity
import com.ute.co_study.feature.learner.ui.markCurrentLearnerTab
import com.ute.co_study.feature.learner.ui.showLearnerFeatureUnavailable
import com.ute.co_study.feature.learner.viewmodel.home.HomeViewModel

class HomeActivity : AppCompatActivity() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = intent.getStringExtra(EXTRA_EMAIL)
        if (email.isNullOrBlank()) {
            finish()
            return
        }

        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)
        ViewCompat.setAccessibilityHeading(binding.suggestionsTitle, true)

        val name = intent.getStringExtra(EXTRA_NAME).orEmpty().trim()
            .takeLastWhile { !it.isWhitespace() }
        binding.screenTitle.text = if (name.isEmpty()) {
            getString(R.string.home_greeting)
        } else {
            getString(R.string.home_greeting_named, name)
        }
        binding.heroDescription.text = heroDescription(viewModel.loadProfile(email))

        val navigation = binding.learnerNavigation
        navigation.homeTab.markCurrentLearnerTab()
        binding.discoverButton.setOnClickListener { openDiscovery() }
        navigation.matchTab.setOnClickListener { openDiscovery() }
        showLearnerFeatureUnavailable(
            binding.notificationsButton,
            navigation.messagesTab,
            navigation.scheduleTab,
            navigation.profileTab
        )
    }

    private fun openDiscovery() {
        val discovery = Intent(this, DiscoveryActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(discovery)
    }

    private fun heroDescription(profile: ProfileSetupDraft): String {
        val share = profile.skillsToShare
        val learn = profile.skillsToLearn
        return when {
            share.isNotEmpty() && learn.isNotEmpty() -> getString(R.string.home_hero_skills, share, learn)
            share.isNotEmpty() -> getString(R.string.home_hero_share, share)
            learn.isNotEmpty() -> getString(R.string.home_hero_learn, learn)
            else -> getString(R.string.home_hero_description)
        }
    }

    companion object {
        private const val EXTRA_EMAIL = "email"
        private const val EXTRA_NAME = "name"

        internal fun createIntent(context: Context, email: String, name: String): Intent =
            Intent(context, HomeActivity::class.java)
                .putExtra(EXTRA_EMAIL, email)
                .putExtra(EXTRA_NAME, name)
    }
}
