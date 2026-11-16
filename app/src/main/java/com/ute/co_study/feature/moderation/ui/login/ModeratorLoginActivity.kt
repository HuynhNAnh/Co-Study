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
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performLogin(binding)
                true
            } else false
        }

        binding.loginButton.setOnClickListener {
            performLogin(binding)
        }
    }

    private fun performLogin(binding: ActivityModeratorLoginBinding) {
        val email = binding.modCodeInput.text.toString().trim()
        val password = binding.passwordInput.text.toString()

        if (!email.equals(MODERATOR_DEMO_EMAIL, ignoreCase = true)) {
            binding.modCodeInput.error = getString(R.string.mod_sample_email_error, MODERATOR_DEMO_EMAIL)
            binding.modCodeInput.requestFocus()
            return
        }
        if (password != STAFF_DEMO_PASSWORD) {
            binding.passwordInput.error = getString(R.string.mod_sample_password_error, STAFF_DEMO_PASSWORD)
            binding.passwordInput.requestFocus()
            return
        }

        DemoSession(this).start(DemoRole.MODERATOR, MODERATOR_DEMO_EMAIL, MODERATOR_DEMO_CODE)
        val intent = Intent(this, ModerationOverviewActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}
