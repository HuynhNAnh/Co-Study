package com.ute.co_study.feature.learner.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityHomeBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.learner.model.ProfileSetupDraft
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
        ViewCompat.setStateDescription(binding.homeTab, getString(R.string.home_tab_current))

        val name = intent.getStringExtra(EXTRA_NAME).orEmpty().trim()
            .takeLastWhile { !it.isWhitespace() }
        binding.screenTitle.text = if (name.isEmpty()) {
            getString(R.string.home_greeting)
        } else {
            getString(R.string.home_greeting_named, name)
        }
        binding.heroDescription.text = heroDescription(viewModel.loadProfile(email))

        val upcomingFeatures = listOf(
            binding.notificationsButton,
            binding.discoverButton,
            binding.matchTab,
            binding.messagesTab,
            binding.scheduleTab,
            binding.profileTab
        )
        upcomingFeatures.forEach { button ->
            button.setOnClickListener {
                Toast.makeText(this, R.string.home_feature_unavailable, Toast.LENGTH_SHORT).show()
            }
        }
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
