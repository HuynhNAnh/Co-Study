package com.ute.co_study.feature.learner.ui.exchange

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityExchangeConnectedBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.learner.ui.home.HomeActivity
import com.ute.co_study.feature.learner.ui.study.ConversationActivity

class ExchangeConnectedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityExchangeConnectedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)
        ViewCompat.setAccessibilityHeading(binding.connectionTitle, true)

        val sharedSkill = intent.getStringExtra(EXTRA_SHARED_SKILL).orEmpty()
        binding.sharedSkill.text = getString(
            R.string.exchange_pending_shared_skill,
            sharedSkill.ifBlank { getString(R.string.exchange_pending_no_skill) }
        )

        binding.homeButton.setOnClickListener {
            val home = Intent(this, HomeActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(home)
        }
        binding.messageButton.setOnClickListener { startActivity(Intent(this, ConversationActivity::class.java)) }
    }

    companion object {
        private const val EXTRA_SHARED_SKILL = "shared_skill"

        internal fun createIntent(context: Context, sharedSkill: String): Intent =
            Intent(context, ExchangeConnectedActivity::class.java).putExtra(EXTRA_SHARED_SKILL, sharedSkill)
    }
}
