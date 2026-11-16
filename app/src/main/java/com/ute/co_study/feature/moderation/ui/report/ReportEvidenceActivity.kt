package com.ute.co_study.feature.moderation.ui.report

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.databinding.ActivityReportEvidenceBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets

class ReportEvidenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityReportEvidenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

        binding.backButton.setOnClickListener { finish() }
        binding.requestMoreInfoButton.setOnClickListener {
            startActivity(Intent(this, RequestMoreInfoActivity::class.java))
        }
        binding.dismissButton.setOnClickListener {
            startActivity(Intent(this, DismissReportActivity::class.java))
        }
    }
}
