package com.ute.co_study.feature.moderation.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.databinding.ActivityModeratorProfileBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.moderation.ui.markCurrentModTab
import com.ute.co_study.feature.moderation.ui.setupModerationNavigation

class ModeratorProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityModeratorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

        binding.moderationNavigation.accountTab.markCurrentModTab()
        setupModerationNavigation(this, binding.moderationNavigation)

        binding.logoutButton.setOnClickListener {
            startActivity(Intent(this, ModeratorLogoutActivity::class.java))
        }
    }
}
