package com.ute.co_study.feature.auth.viewmodel.login

import androidx.lifecycle.ViewModel
import com.ute.co_study.R
import com.ute.co_study.feature.auth.viewmodel.validateEmail

class LoginViewModel : ViewModel() {
    fun validate(email: String, password: String): LoginUiState = LoginUiState(
        emailError = validateEmail(email),
        passwordError = R.string.auth_password_required.takeIf { password.isBlank() }
    )
}
