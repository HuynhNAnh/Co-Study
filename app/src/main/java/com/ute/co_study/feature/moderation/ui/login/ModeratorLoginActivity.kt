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

class ModeratorLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityModeratorLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

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
        val code = binding.modCodeInput.text.toString().trim()
        val pass = binding.passwordInput.text.toString().trim()

        if (code.isBlank()) {
            binding.modCodeInput.error = getString(R.string.mod_login_code_required)
            binding.modCodeInput.requestFocus()
            return
        }
        if (pass.isBlank()) {
            binding.passwordInput.error = getString(R.string.auth_password_required)
            binding.passwordInput.requestFocus()
            return
        }

        val intent = Intent(this, ModerationOverviewActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}
