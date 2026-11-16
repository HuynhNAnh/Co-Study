package com.ute.co_study.feature.learner.ui.exchange

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityExchangeRequestBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.learner.data.repository.ExchangeQuota
import com.ute.co_study.feature.learner.model.SamplePartners
import com.ute.co_study.feature.learner.ui.study.studyConversation

class ExchangeRequestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityExchangeRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)
        ViewCompat.setAccessibilityHeading(binding.partnerName, true)

        val conversation = intent.studyConversation()
        val partner = SamplePartners.find(conversation) ?: return finish()
        binding.partnerInitials.text = conversation.initials
        binding.partnerName.text = conversation.title
        binding.partnerSubtitle.text = conversation.subtitle
        binding.shareInput.hint = partner.wants
        binding.learnInput.hint = partner.shares
        binding.messageInput.hint = getString(R.string.exchange_request_message_hint, conversation.title)

        binding.backButton.setOnClickListener { finish() }
        val quota = ExchangeQuota(this)
        binding.sendButton.setOnClickListener {
            if (!quota.consume()) {
                Toast.makeText(this, "Bạn đã hết lượt gửi lời mời hôm nay hoặc phiên đã kết thúc.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val pending = ExchangePendingActivity.createIntent(
                this,
                conversation,
                sharedSkill = binding.shareInput.text.toString().trim(),
                learningSkill = binding.learnInput.text.toString().trim(),
                message = binding.messageInput.text.toString().trim()
            ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            WindowCompat.getInsetsController(window, binding.root).hide(WindowInsetsCompat.Type.ime())
            startActivity(pending)
        }
    }
}
