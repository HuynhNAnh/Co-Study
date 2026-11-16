package com.ute.co_study.feature.learner.ui.exchange

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityExchangePendingBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.learner.ui.discovery.DiscoveryActivity
import com.ute.co_study.feature.learner.ui.home.HomeActivity

class ExchangePendingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityExchangePendingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)
        ViewCompat.setAccessibilityHeading(binding.partnerName, true)

        val sharedSkill = intent.getStringExtra(EXTRA_SHARED_SKILL).orEmpty()
        val learningSkill = intent.getStringExtra(EXTRA_LEARNING_SKILL).orEmpty()
        binding.sharedSkill.text = getString(
            R.string.exchange_pending_shared_skill,
            sharedSkill.ifBlank { getString(R.string.exchange_pending_no_skill) }
        )
        binding.learningSkill.text = getString(
            R.string.exchange_pending_learning_skill,
            learningSkill.ifBlank { getString(R.string.exchange_pending_no_skill) }
        )
        binding.requestMessage.text = intent.getStringExtra(EXTRA_MESSAGE).orEmpty()
            .ifBlank { getString(R.string.exchange_pending_no_message) }

        binding.backButton.setOnClickListener { finish() }
        binding.homeButton.setOnClickListener { returnTo(HomeActivity::class.java) }
        binding.discoverButton.setOnClickListener { returnTo(DiscoveryActivity::class.java) }
    }

    private fun returnTo(destination: Class<out AppCompatActivity>) {
        val previousScreen = Intent(this, destination)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(previousScreen)
    }

    companion object {
        private const val EXTRA_SHARED_SKILL = "shared_skill"
        private const val EXTRA_LEARNING_SKILL = "learning_skill"
        private const val EXTRA_MESSAGE = "message"

        internal fun createIntent(
            context: Context,
            sharedSkill: String,
            learningSkill: String,
            message: String
        ): Intent = Intent(context, ExchangePendingActivity::class.java)
            .putExtra(EXTRA_SHARED_SKILL, sharedSkill)
            .putExtra(EXTRA_LEARNING_SKILL, learningSkill)
            .putExtra(EXTRA_MESSAGE, message)
    }
}
