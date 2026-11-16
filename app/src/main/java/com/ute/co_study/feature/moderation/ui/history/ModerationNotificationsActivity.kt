package com.ute.co_study.feature.moderation.ui.history

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.databinding.ActivityModerationNotificationsBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.moderation.ui.report.ReportDetailActivity
import com.ute.co_study.feature.moderation.ui.report.ReportListActivity

class ModerationNotificationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityModerationNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

        binding.backButton.setOnClickListener { finish() }
        binding.backToOverviewButton.setOnClickListener { finish() }

        binding.notificationItem1.setOnClickListener {
            startActivity(Intent(this, ReportDetailActivity::class.java))
        }

        binding.notificationItem2.setOnClickListener {
            startActivity(Intent(this, ReportListActivity::class.java))
        }
    }
}
