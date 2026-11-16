package com.ute.co_study.feature.auth.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.widget.doAfterTextChanged
import com.ute.co_study.R
import com.ute.co_study.feature.learner.data.repository.ProfileSetupRepository
import com.ute.co_study.feature.learner.ui.home.HomeActivity
import com.ute.co_study.shared.data.DemoSession
import com.ute.co_study.databinding.ActivityLoginBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.auth.ui.register.RegisterActivity
import com.ute.co_study.feature.auth.ui.reset.ResetPasswordActivity
import com.ute.co_study.feature.auth.viewmodel.login.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

        binding.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.loginButton.setOnClickListener { submit(binding) }
        binding.passwordInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_DONE) {
                return@setOnEditorActionListener false
            }
            submit(binding)
            true
        }
        binding.emailInput.doAfterTextChanged { binding.emailInput.error = null }
        binding.passwordInput.doAfterTextChanged { binding.passwordInput.error = null }
        binding.forgotPasswordButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            startActivity(ResetPasswordActivity.createIntent(this, email))
        }
        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun openLocalProfile(email: String) {
        val session = DemoSession(this)
        val savedEmail = session.learnerEmail(email) ?: email.takeIf { ProfileSetupRepository(this).hasDraft(it) }
        if (savedEmail == null) {
            Toast.makeText(this, R.string.auth_login_unavailable, Toast.LENGTH_LONG).show()
            return
        }
        val name = session.learnerName(savedEmail).ifBlank { savedEmail.substringBefore('@') }
        startActivity(HomeActivity.createIntent(this, savedEmail, name)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
    }

    private fun submit(binding: ActivityLoginBinding) {
        val state = viewModel.validate(
            binding.emailInput.text.toString(),
            binding.passwordInput.text.toString()
        )
        binding.emailInput.error = state.emailError?.let { getString(it) }
        binding.passwordInput.error = state.passwordError?.let { getString(it) }
        when {
            state.emailError != null -> binding.emailInput.requestFocus()
            state.passwordError != null -> binding.passwordInput.requestFocus()
            else -> openLocalProfile(binding.emailInput.text.toString().trim())
        }
    }
}
