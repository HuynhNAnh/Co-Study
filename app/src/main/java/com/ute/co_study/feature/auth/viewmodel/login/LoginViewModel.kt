package com.ute.co_study.feature.auth.viewmodel.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.ute.co_study.R

class LoginViewModel : ViewModel() {
    fun validate(email: String, password: String): LoginUiState = LoginUiState(
        emailError = when {
            email.isBlank() -> R.string.login_email_required
            !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> R.string.login_email_invalid
            else -> null
        },
        passwordError = R.string.login_password_required.takeIf { password.isBlank() }
    )
}
