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
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityProfileSetupBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.learner.model.ProfileSetupDraft
import com.ute.co_study.feature.learner.viewmodel.profile.ProfileSetupViewModel

class ProfileSetupActivity : AppCompatActivity() {
    private val viewModel: ProfileSetupViewModel by viewModels()

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

        if (savedInstanceState == null) {
            val draft = viewModel.loadDraft(email)
            binding.skillsToShareInput.setText(draft.skillsToShare)
            binding.skillsToLearnInput.setText(draft.skillsToLearn)
            binding.learningGoalInput.setText(draft.learningGoal)
            binding.availabilityInput.setText(draft.availability)
        }

        binding.completeButton.setOnClickListener {
            val draft = ProfileSetupDraft(
                skillsToShare = binding.skillsToShareInput.text.toString().trim(),
                skillsToLearn = binding.skillsToLearnInput.text.toString().trim(),
                learningGoal = binding.learningGoalInput.text.toString().trim(),
                availability = binding.availabilityInput.text.toString().trim()
            )
            viewModel.saveDraft(email, draft)
            WindowCompat.getInsetsController(window, binding.root).hide(WindowInsetsCompat.Type.ime())
            Toast.makeText(this, R.string.profile_setup_saved_locally, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val EXTRA_EMAIL = "email"

        internal fun createIntent(context: Context, email: String): Intent =
            Intent(context, ProfileSetupActivity::class.java).putExtra(EXTRA_EMAIL, email.trim())
    }
}
