package com.ute.co_study.feature.learner.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.databinding.ActivityPartnerProfileBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.learner.ui.exchange.ExchangeRequestActivity

class PartnerProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPartnerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)
        ViewCompat.setAccessibilityHeading(binding.partnerName, true)

        binding.backButton.setOnClickListener { finish() }
        binding.exchangeButton.setOnClickListener {
            val request = Intent(this, ExchangeRequestActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(request)
        }
        binding.reportButton.setOnClickListener { startActivity(Intent(this, ReportPartnerActivity::class.java)) }
    }
}
