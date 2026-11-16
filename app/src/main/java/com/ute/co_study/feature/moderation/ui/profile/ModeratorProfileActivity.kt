package com.ute.co_study.feature.moderation.ui.profile

import com.ute.co_study.databinding.ActivityModeratorProfileBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import com.ute.co_study.R
import com.ute.co_study.feature.moderation.ui.setupModerationNavigation

class ModeratorProfileActivity : ModerationActivity<ActivityModeratorProfileBinding>(ActivityModeratorProfileBinding::inflate) {
    override fun bind() {
        binding.screenSubtitle.text = getString(R.string.mod_operator_status, session.name)
        binding.operatorEmail.text = session.email
        setupModerationNavigation(this, binding.moderationNavigation)
        binding.logoutButton.setOnClickListener { open(ModeratorLogoutActivity::class.java) }
    }
}
