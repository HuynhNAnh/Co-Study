package com.ute.co_study.feature.auth.ui.reset

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.widget.doAfterTextChanged
import com.ute.co_study.databinding.ActivityResetPasswordBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.auth.viewmodel.reset.ResetPasswordViewModel

class ResetPasswordActivity : AppCompatActivity() {
    private val viewModel: ResetPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)
        ViewCompat.setAccessibilityHeading(binding.resetIntroTitle, true)

        val confirmation = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            binding.sendLinkButton.isEnabled = true
            when (result.resultCode) {
                ResetLinkSentActivity.RESULT_RETURN_TO_LOGIN -> finish()
                ResetLinkSentActivity.RESULT_CHANGE_EMAIL -> {
                    binding.emailInput.text?.clear()
                    binding.emailInput.requestFocus()
                }
            }
        }
        val sendLink = {
            if (binding.sendLinkButton.isEnabled) {
                submit(binding) { email ->
                    binding.sendLinkButton.isEnabled = false
                    confirmation.launch(ResetLinkSentActivity.createIntent(this, email))
                }
            }
        }

        binding.backButton.setOnClickListener { finish() }
        binding.loginButton.setOnClickListener { finish() }
        binding.sendLinkButton.setOnClickListener { sendLink() }
        binding.emailInput.doAfterTextChanged { binding.emailInput.error = null }
        binding.emailInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_DONE) {
                return@setOnEditorActionListener false
            }
            sendLink()
            true
        }
    }

    private fun submit(binding: ActivityResetPasswordBinding, onValidEmail: (String) -> Unit) {
        val email = binding.emailInput.text.toString().trim()
        val error = viewModel.validate(email)
        if (error != null) {
            binding.emailInput.error = getString(error)
            binding.emailInput.requestFocus()
            return
        }
        onValidEmail(email)
    }
}
