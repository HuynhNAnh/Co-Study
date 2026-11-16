package com.ute.co_study.feature.auth.viewmodel

import android.util.Patterns
import androidx.annotation.StringRes
import com.ute.co_study.R

@StringRes
internal fun validateEmail(email: String): Int? = when {
    email.isBlank() -> R.string.auth_email_required
    !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> R.string.auth_email_invalid
    else -> null
}
