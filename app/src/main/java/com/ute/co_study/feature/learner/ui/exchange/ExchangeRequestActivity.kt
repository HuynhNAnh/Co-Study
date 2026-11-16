package com.ute.co_study.feature.learner.ui.exchange

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.ute.co_study.databinding.ActivityExchangeRequestBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets

class ExchangeRequestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityExchangeRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)
        ViewCompat.setAccessibilityHeading(binding.partnerName, true)

        binding.backButton.setOnClickListener { finish() }
        binding.sendButton.setOnClickListener {
            val pending = ExchangePendingActivity.createIntent(
                this,
                sharedSkill = binding.shareInput.text.toString().trim(),
                learningSkill = binding.learnInput.text.toString().trim(),
                message = binding.messageInput.text.toString().trim()
            ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            WindowCompat.getInsetsController(window, binding.root).hide(WindowInsetsCompat.Type.ime())
            startActivity(pending)
        }
    }
}
