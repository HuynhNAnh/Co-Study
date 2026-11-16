package com.ute.co_study.feature.moderation.ui.report

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.databinding.ActivityReportDetailBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets

class ReportDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityReportDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

        binding.backButton.setOnClickListener { finish() }
        binding.acceptButton.setOnClickListener {
            val intent = android.content.Intent(this, ReportEvidenceActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
