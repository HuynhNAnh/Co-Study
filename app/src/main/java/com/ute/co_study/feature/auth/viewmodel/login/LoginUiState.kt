package com.ute.co_study.feature.auth.viewmodel.login

import androidx.annotation.StringRes

data class LoginUiState(
    @get:StringRes val emailError: Int?,
    @get:StringRes val passwordError: Int?
)
