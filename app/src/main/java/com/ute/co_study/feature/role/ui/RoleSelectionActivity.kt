package com.ute.co_study.feature.role.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.databinding.ActivityRoleSelectionBinding
import com.ute.co_study.feature.admin.ui.AdminActivity
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.learner.ui.home.HomeActivity
import com.ute.co_study.feature.moderation.ui.login.ModeratorLoginActivity

/** Entry point for selecting a prototype role without requiring a backend account. */
class RoleSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRoleSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

        binding.learnerRoleCard.setOnClickListener {
            startActivity(HomeActivity.createIntent(this, "hocvien@costudy.vn", "Học viên"))
        }
        binding.moderatorRoleCard.setOnClickListener {
            startActivity(Intent(this, ModeratorLoginActivity::class.java))
        }
        binding.adminRoleCard.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }
    }
}
