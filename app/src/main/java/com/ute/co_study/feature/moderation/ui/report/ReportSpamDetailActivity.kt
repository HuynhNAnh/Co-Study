package com.ute.co_study.feature.moderation.ui.report

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.databinding.ActivityReportSpamDetailBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets

class ReportSpamDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityReportSpamDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

        binding.backButton.setOnClickListener { finish() }

        binding.chooseActionButton.setOnClickListener {
            startActivity(Intent(this, ActionOptionsActivity::class.java))
        }

        binding.escalateAdminButton.setOnClickListener {
            startActivity(Intent(this, EscalateAdminActivity::class.java))
        }
    }
}
