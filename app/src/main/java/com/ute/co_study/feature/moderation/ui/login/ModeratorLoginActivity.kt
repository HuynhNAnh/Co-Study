package com.ute.co_study.feature.moderation.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.widget.doAfterTextChanged
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityModeratorLoginBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.moderation.ui.overview.ModerationOverviewActivity
import com.ute.co_study.shared.data.DemoMemberDirectory
import com.ute.co_study.shared.data.DemoRole
import com.ute.co_study.shared.data.DemoSession
import com.ute.co_study.shared.data.MODERATOR_DEMO_CODE
import com.ute.co_study.shared.data.MODERATOR_DEMO_EMAIL
import com.ute.co_study.shared.data.STAFF_DEMO_PASSWORD

class ModeratorLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityModeratorLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

        binding.modCodeInput.setText(MODERATOR_DEMO_EMAIL)
        binding.passwordInput.setText(STAFF_DEMO_PASSWORD)
        binding.modCodeInput.doAfterTextChanged { binding.modCodeInput.error = null }
        binding.passwordInput.doAfterTextChanged { binding.passwordInput.error = null }
        binding.passwordInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_DONE) return@setOnEditorActionListener false
            submit(binding)
            true
        }
        binding.loginButton.setOnClickListener { submit(binding) }
    }

    private fun submit(binding: ActivityModeratorLoginBinding) {
        val email = binding.modCodeInput.text.toString().trim()
        val operator = if (email.equals(MODERATOR_DEMO_EMAIL, ignoreCase = true)) {
            MODERATOR_DEMO_CODE
        } else {
            DemoMemberDirectory(this).findByEmail(email)
                ?.takeIf { it.role == DemoRole.MODERATOR && !it.locked }?.id
        }
        if (operator == null) {
            binding.modCodeInput.error = getString(R.string.mod_login_account_error, MODERATOR_DEMO_EMAIL)
            binding.modCodeInput.requestFocus()
            return
        }
        if (binding.passwordInput.text.toString() != STAFF_DEMO_PASSWORD) {
            binding.passwordInput.error = getString(R.string.mod_login_password_error, STAFF_DEMO_PASSWORD)
            binding.passwordInput.requestFocus()
            return
        }

        DemoSession(this).start(DemoRole.MODERATOR, email, operator)
        startActivity(Intent(this, ModerationOverviewActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
    }
}
