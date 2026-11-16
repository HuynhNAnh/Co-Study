package com.ute.co_study.feature.auth.viewmodel.register

import androidx.lifecycle.ViewModel
import com.ute.co_study.R
import com.ute.co_study.feature.auth.viewmodel.validateEmail

class RegisterViewModel : ViewModel() {
    fun validate(name: String, email: String, password: String, confirmation: String): RegisterUiState =
        RegisterUiState(
            nameError = R.string.register_name_required.takeIf { name.isBlank() },
            emailError = validateEmail(email),
            passwordError = R.string.auth_password_required.takeIf { password.isBlank() },
            confirmationError = when {
                confirmation.isBlank() -> R.string.register_confirmation_required
                confirmation != password -> R.string.register_confirmation_mismatch
                else -> null
            }
        )
}
