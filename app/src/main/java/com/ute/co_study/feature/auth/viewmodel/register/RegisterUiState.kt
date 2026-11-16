package com.ute.co_study.feature.auth.viewmodel.register

import androidx.annotation.StringRes

data class RegisterUiState(
    @get:StringRes val nameError: Int?,
    @get:StringRes val emailError: Int?,
    @get:StringRes val passwordError: Int?,
    @get:StringRes val confirmationError: Int?
)
