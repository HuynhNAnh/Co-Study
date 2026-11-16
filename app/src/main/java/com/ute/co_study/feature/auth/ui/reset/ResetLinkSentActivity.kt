package com.ute.co_study.feature.auth.ui.reset

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.databinding.ActivityResetLinkSentBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets

class ResetLinkSentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = intent.getStringExtra(EXTRA_EMAIL)
        if (email.isNullOrBlank()) {
            finish()
            return
        }

        val binding = ActivityResetLinkSentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)
        ViewCompat.setAccessibilityHeading(binding.confirmationTitle, true)

        binding.receivingEmail.text = email
        binding.backButton.setOnClickListener { finish() }
        binding.loginButton.setOnClickListener {
            setResult(RESULT_RETURN_TO_LOGIN)
            finish()
        }
        binding.changeEmailButton.setOnClickListener {
            setResult(RESULT_CHANGE_EMAIL)
            finish()
        }
    }

    companion object {
        private const val EXTRA_EMAIL = "email"
        internal const val RESULT_RETURN_TO_LOGIN = RESULT_FIRST_USER
        internal const val RESULT_CHANGE_EMAIL = RESULT_FIRST_USER + 1

        internal fun createIntent(context: Context, email: String): Intent =
            Intent(context, ResetLinkSentActivity::class.java).putExtra(EXTRA_EMAIL, email)
    }
}
