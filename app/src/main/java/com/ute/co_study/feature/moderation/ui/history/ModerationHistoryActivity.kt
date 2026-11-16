package com.ute.co_study.feature.moderation.ui.history

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.databinding.ActivityModerationHistoryBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.moderation.ui.markCurrentModTab
import com.ute.co_study.feature.moderation.ui.setupModerationNavigation

class ModerationHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityModerationHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

        binding.moderationNavigation.historyTab.markCurrentModTab()
        setupModerationNavigation(this, binding.moderationNavigation)

        binding.historyItem018.setOnClickListener {
            startActivity(Intent(this, HistoryDetailActivity::class.java))
        }
    }
}
