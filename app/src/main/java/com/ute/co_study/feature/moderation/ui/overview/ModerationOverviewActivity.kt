package com.ute.co_study.feature.moderation.ui.overview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.databinding.ActivityModerationOverviewBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.moderation.ui.history.ModerationNotificationsActivity
import com.ute.co_study.feature.moderation.ui.markCurrentModTab
import com.ute.co_study.feature.moderation.ui.report.ReportDetailActivity
import com.ute.co_study.feature.moderation.ui.report.ReportListActivity
import com.ute.co_study.feature.moderation.ui.report.ReportSpamDetailActivity
import com.ute.co_study.feature.moderation.ui.setupModerationNavigation

class ModerationOverviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityModerationOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

        binding.moderationNavigation.overviewTab.markCurrentModTab()
        setupModerationNavigation(this, binding.moderationNavigation)

        binding.report001Card.setOnClickListener {
            startActivity(Intent(this, ReportDetailActivity::class.java))
        }

        binding.report002Card.setOnClickListener {
            startActivity(Intent(this, ReportSpamDetailActivity::class.java))
        }

        binding.notificationsBanner.setOnClickListener {
            startActivity(Intent(this, ModerationNotificationsActivity::class.java))
        }

        binding.seeAllButton.setOnClickListener {
            startActivity(Intent(this, ReportListActivity::class.java))
        }
    }
}
