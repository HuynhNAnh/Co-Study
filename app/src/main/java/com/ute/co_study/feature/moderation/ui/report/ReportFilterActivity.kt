package com.ute.co_study.feature.moderation.ui.report

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.databinding.ActivityReportFilterBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets

class ReportFilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityReportFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

        binding.backButton.setOnClickListener { finish() }
        binding.backListButton.setOnClickListener { finish() }

        binding.applyFilterButton.setOnClickListener {
            finish()
        }
    }
}
