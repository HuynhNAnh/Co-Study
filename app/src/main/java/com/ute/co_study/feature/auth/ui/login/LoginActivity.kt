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
import com.ute.co_study.databinding.ActivityLoginBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.auth.ui.register.RegisterActivity
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
            Toast.makeText(this, R.string.login_reset_unavailable, Toast.LENGTH_SHORT).show()
        }
        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
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
            else -> Toast.makeText(this, R.string.auth_login_unavailable, Toast.LENGTH_LONG).show()
        }
    }
}
