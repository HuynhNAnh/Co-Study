package com.ute.co_study.feature.role.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.databinding.ActivityRoleSelectionBinding
import com.ute.co_study.feature.admin.ui.AdminLoginActivity
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.auth.ui.welcome.WelcomeActivity
import com.ute.co_study.feature.learner.ui.home.HomeActivity
import com.ute.co_study.feature.moderation.ui.login.ModeratorLoginActivity
import com.ute.co_study.shared.data.DemoSession

class RoleSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRoleSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

        binding.learnerRoleCard.setOnClickListener {
            val name = DemoSession(this).learnerName(DEMO_LEARNER_EMAIL).ifBlank { "Học viên" }
            startActivity(HomeActivity.createIntent(this, DEMO_LEARNER_EMAIL, name))
        }
        binding.learnerAccountButton.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
        }
        binding.moderatorRoleCard.setOnClickListener {
            startActivity(Intent(this, ModeratorLoginActivity::class.java))
        }
        binding.adminRoleCard.setOnClickListener {
            startActivity(Intent(this, AdminLoginActivity::class.java))
        }
    }

    private companion object {
        const val DEMO_LEARNER_EMAIL = "hocvien@costudy.example"
    }
}
