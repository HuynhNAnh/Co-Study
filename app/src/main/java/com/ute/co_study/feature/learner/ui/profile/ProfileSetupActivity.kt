package com.ute.co_study.feature.learner.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityProfileSetupBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.learner.ui.home.HomeActivity
import com.ute.co_study.feature.learner.viewmodel.profile.ProfileSetupViewModel
import com.ute.co_study.feature.learner.viewmodel.profile.ProfileSetupViewModel.SaveState
import kotlinx.coroutines.launch

class ProfileSetupActivity : AppCompatActivity() {
    private val viewModel: ProfileSetupViewModel by viewModels()
    private lateinit var fields: LearnerProfileFields

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = intent.getStringExtra(EXTRA_EMAIL)
        if (email.isNullOrBlank()) {
            finish()
            return
        }

        val binding = ActivityProfileSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)
        binding.backButton.setOnClickListener { finish() }

        fields = LearnerProfileFields(
            this, binding.profileFields, viewModel.skillNames(), viewModel.loadDraft(email),
            viewModel.evidence(email), savedInstanceState
        )

        val name = intent.getStringExtra(EXTRA_NAME).orEmpty()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.saveState.collect { state ->
                    binding.completeButton.isEnabled = state == SaveState.IDLE
                    binding.completeButton.setText(
                        if (state == SaveState.SAVING) R.string.profile_setup_saving
                        else R.string.profile_setup_complete
                    )
                    when (state) {
                        SaveState.SAVED -> {
                            val home = HomeActivity.createIntent(this@ProfileSetupActivity, email, name)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(home)
                        }

                        SaveState.FAILED -> {
                            Toast.makeText(
                                this@ProfileSetupActivity,
                                R.string.profile_setup_save_failed,
                                Toast.LENGTH_LONG
                            ).show()
                            viewModel.acknowledgeSaveFailure()
                        }

                        SaveState.IDLE, SaveState.SAVING -> Unit
                    }
                }
            }
        }

        binding.completeButton.setOnClickListener {
            val draft = fields.draft() ?: return@setOnClickListener
            viewModel.saveDraft(email, name, draft)
            WindowCompat.getInsetsController(window, binding.root).hide(WindowInsetsCompat.Type.ime())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::fields.isInitialized) fields.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val EXTRA_EMAIL = "email"
        private const val EXTRA_NAME = "name"

        internal fun createIntent(context: Context, email: String, name: String): Intent =
            Intent(context, ProfileSetupActivity::class.java)
                .putExtra(EXTRA_EMAIL, email.trim())
                .putExtra(EXTRA_NAME, name.trim())
    }
}
