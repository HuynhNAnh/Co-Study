package com.ute.co_study.feature.learner.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.ute.co_study.shared.data.DemoMemberDirectory
import com.ute.co_study.feature.auth.ui.welcome.WelcomeActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.shared.data.DemoRole
import com.ute.co_study.shared.data.DemoSession
import com.ute.co_study.feature.learner.ui.study.MessagesActivity
import com.ute.co_study.feature.learner.ui.study.ScheduleActivity
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityHomeBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.learner.model.ProfileSetupDraft
import com.ute.co_study.feature.learner.ui.discovery.DiscoveryActivity
import com.ute.co_study.feature.learner.ui.markCurrentLearnerTab
import com.ute.co_study.feature.learner.ui.profile.LearnerProfileActivity
import com.ute.co_study.feature.learner.ui.profile.NotificationsActivity
import com.ute.co_study.feature.learner.viewmodel.home.HomeViewModel

class HomeActivity : AppCompatActivity() {
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val session = DemoSession(this)
        val incomingEmail = intent.getStringExtra(EXTRA_EMAIL)
        if (!incomingEmail.isNullOrBlank()) {
            val managed = DemoMemberDirectory(this).findByEmail(incomingEmail)
            if (managed?.locked == true || managed?.role == DemoRole.MODERATOR) {
                session.end()
                Toast.makeText(this, R.string.auth_managed_unavailable, Toast.LENGTH_LONG).show()
                startActivity(Intent(this, WelcomeActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                finish()
                return
            }
            session.start(DemoRole.LEARNER, incomingEmail, intent.getStringExtra(EXTRA_NAME).orEmpty())
            intent.removeExtra(EXTRA_EMAIL)
            intent.removeExtra(EXTRA_NAME)
        }
        val email = session.email.takeIf { session.role == DemoRole.LEARNER }
        if (email.isNullOrBlank()) {
            finish()
            return
        }

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)
        ViewCompat.setAccessibilityHeading(binding.suggestionsTitle, true)

        val navigation = binding.learnerNavigation
        navigation.homeTab.markCurrentLearnerTab()
        binding.discoverButton.setOnClickListener { openDiscovery() }
        navigation.matchTab.setOnClickListener { openDiscovery() }
        navigation.messagesTab.setOnClickListener { startActivity(Intent(this, MessagesActivity::class.java)) }
        navigation.scheduleTab.setOnClickListener { startActivity(Intent(this, ScheduleActivity::class.java)) }
        binding.notificationsButton.setOnClickListener { startActivity(Intent(this, NotificationsActivity::class.java)) }
        navigation.profileTab.setOnClickListener { startActivity(Intent(this, LearnerProfileActivity::class.java)) }
    }

    override fun onResume() {
        super.onResume()
        if (!::binding.isInitialized) return
        val session = DemoSession(this)
        val name = session.name.trim().takeLastWhile { !it.isWhitespace() }
        binding.screenTitle.text = if (name.isEmpty()) getString(R.string.home_greeting)
            else getString(R.string.home_greeting_named, name)
        binding.heroDescription.text = heroDescription(viewModel.loadProfile(session.email))
    }

    private fun openDiscovery() {
        val discovery = Intent(this, DiscoveryActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(discovery)
    }

    private fun heroDescription(profile: ProfileSetupDraft): String {
        val share = profile.skillsToShare.joinToString(", ")
        val learn = profile.skillsToLearn.joinToString(", ")
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
