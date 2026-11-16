package com.ute.co_study.feature.moderation.ui.profile

import com.ute.co_study.databinding.ActivityModeratorLogoutBinding
import com.ute.co_study.feature.moderation.ui.ModerationActivity
import android.content.Intent
import com.ute.co_study.R
import com.ute.co_study.feature.role.ui.RoleSelectionActivity

class ModeratorLogoutActivity : ModerationActivity<ActivityModeratorLogoutBinding>(ActivityModeratorLogoutBinding::inflate) {
    override fun bind() {
        binding.currentOperator.text = getString(R.string.mod_report_status, session.name, session.email)
        binding.continueWorkButton.setOnClickListener { finish() }
        binding.confirmLogoutButton.setOnClickListener {
            session.end()
            startActivity(Intent(this, RoleSelectionActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        }
    }
}
