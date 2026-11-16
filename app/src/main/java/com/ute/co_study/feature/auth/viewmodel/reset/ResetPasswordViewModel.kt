package com.ute.co_study.feature.auth.viewmodel.reset

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.ute.co_study.feature.auth.viewmodel.validateEmail

class ResetPasswordViewModel : ViewModel() {
    @StringRes
    fun validate(email: String): Int? = validateEmail(email)
}
