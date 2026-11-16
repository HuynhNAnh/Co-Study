package com.ute.co_study.feature.moderation.ui.report

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.databinding.ActivityDismissReportBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets

class DismissReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDismissReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

        binding.backButton.setOnClickListener { finish() }
        binding.backReviewButton.setOnClickListener { finish() }

        binding.confirmDismissButton.setOnClickListener {
            val intent = Intent(this, ReportDismissedActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
            }
            startActivity(intent)
            finish()
        }
    }
}
