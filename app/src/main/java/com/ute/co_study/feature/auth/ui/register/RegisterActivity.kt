package com.ute.co_study.feature.auth.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.widget.doAfterTextChanged
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityRegisterBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.auth.ui.login.LoginActivity
import com.ute.co_study.feature.auth.viewmodel.register.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

        binding.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.loginButton.setOnClickListener {
            val login = Intent(this, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(login)
            finish()
        }
        binding.registerButton.setOnClickListener { submit(binding) }
        binding.confirmationInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_DONE) {
                return@setOnEditorActionListener false
            }
            submit(binding)
            true
        }
        binding.nameInput.doAfterTextChanged { binding.nameInput.error = null }
        binding.emailInput.doAfterTextChanged { binding.emailInput.error = null }
        binding.passwordInput.doAfterTextChanged {
            binding.passwordInput.error = null
            binding.confirmationInput.error = null
        }
        binding.confirmationInput.doAfterTextChanged { binding.confirmationInput.error = null }
    }

    private fun submit(binding: ActivityRegisterBinding) {
        val state = viewModel.validate(
            binding.nameInput.text.toString(),
            binding.emailInput.text.toString(),
            binding.passwordInput.text.toString(),
            binding.confirmationInput.text.toString()
        )
        val fields = listOf(
            binding.nameInput to state.nameError,
            binding.emailInput to state.emailError,
            binding.passwordInput to state.passwordError,
            binding.confirmationInput to state.confirmationError
        )
        fields.forEach { (field, error) -> showError(field, error) }
        val firstInvalidField = fields.firstOrNull { (_, error) -> error != null }?.first
        if (firstInvalidField != null) {
            firstInvalidField.requestFocus()
        } else {
            Toast.makeText(this, R.string.auth_registration_unavailable, Toast.LENGTH_LONG).show()
        }
    }

    private fun showError(field: AppCompatEditText, @StringRes error: Int?) {
        field.error = error?.let { getString(it) }
    }
}
