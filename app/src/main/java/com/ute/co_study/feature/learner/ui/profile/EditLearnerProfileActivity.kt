package com.ute.co_study.feature.learner.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityEditLearnerProfileBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.learner.viewmodel.profile.ProfileSetupViewModel
import com.ute.co_study.feature.learner.viewmodel.profile.ProfileSetupViewModel.SaveState
import com.ute.co_study.shared.data.DemoRole
import com.ute.co_study.shared.data.DemoSession
import kotlinx.coroutines.launch

class EditLearnerProfileActivity : AppCompatActivity() {
    private val viewModel: ProfileSetupViewModel by viewModels()
    private lateinit var fields: LearnerProfileFields

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val session = DemoSession(this)
        val email = session.email
        if (session.role != DemoRole.LEARNER || email.isBlank()) {
            finish()
            return
        }

        val binding = ActivityEditLearnerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.workflowStatus)
        ViewCompat.setAccessibilityHeading(binding.workflowTitle, true)
        fields = LearnerProfileFields(
            this, binding.profileFields, viewModel.skillNames(), viewModel.loadDraft(email),
            viewModel.evidence(email), savedInstanceState
        )
        binding.learnerNameInput.setText(session.name)
        binding.workflowBack.setOnClickListener { finish() }
        binding.cancelButton.setOnClickListener { finish() }
        binding.saveButton.setOnClickListener {
            val name = binding.learnerNameInput.text.toString().trim()
            if (name.isBlank()) {
                binding.learnerNameInput.error = getString(R.string.workflow_required)
                binding.learnerNameInput.requestFocus()
                return@setOnClickListener
            }
            val draft = fields.draft() ?: return@setOnClickListener
            viewModel.saveDraft(email, name, draft)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.saveState.collect { state ->
                    binding.saveButton.isEnabled = state == SaveState.IDLE
                    binding.saveButton.setText(
                        if (state == SaveState.SAVING) R.string.profile_setup_saving else R.string.profile_edit_save
                    )
                    when (state) {
                        SaveState.SAVED -> {
                            if (session.email == email) viewModel.savedName?.let(session::updateName)
                            Toast.makeText(this@EditLearnerProfileActivity, R.string.profile_edit_saved, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        SaveState.FAILED -> {
                            Toast.makeText(this@EditLearnerProfileActivity, R.string.profile_setup_save_failed, Toast.LENGTH_LONG).show()
                            viewModel.acknowledgeSaveFailure()
                        }
                        SaveState.IDLE, SaveState.SAVING -> Unit
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::fields.isInitialized) fields.saveState(outState)
        super.onSaveInstanceState(outState)
    }
}
