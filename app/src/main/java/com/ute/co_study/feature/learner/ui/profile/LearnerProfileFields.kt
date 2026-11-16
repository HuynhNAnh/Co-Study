package com.ute.co_study.feature.learner.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ute.co_study.R
import com.ute.co_study.databinding.ViewLearnerProfileFieldsBinding
import com.ute.co_study.feature.learner.model.ProfileSetupDraft
import com.ute.co_study.shared.model.SkillEvidence
import com.ute.co_study.shared.model.SkillEvidenceAttachment
import com.ute.co_study.shared.model.SkillEvidenceInput

internal class LearnerProfileFields(
    private val activity: AppCompatActivity,
    private val binding: ViewLearnerProfileFieldsBinding,
    suggestions: List<String>,
    profile: ProfileSetupDraft,
    evidence: SkillEvidence?,
    savedInstanceState: Bundle?
) {
    private val sharePicker = SkillPicker(
        binding.skillsToShareInput, binding.sharedSkills, suggestions,
        savedInstanceState?.getStringArrayList(SHARED_SKILLS_STATE) ?: profile.skillsToShare
    )
    private val learnPicker = SkillPicker(
        binding.skillsToLearnInput, binding.learningSkills, suggestions,
        savedInstanceState?.getStringArrayList(LEARNING_SKILLS_STATE) ?: profile.skillsToLearn
    )

    private var attachment = savedInstanceState?.getBundle(ATTACHMENT_STATE)?.let { restoreAttachment(it) }
        ?: profile.evidence.attachment.takeIf { savedInstanceState?.containsKey(ATTACHMENT_STATE) != true }

    private val pickAttachment = activity.registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            val selected = readAttachment(uri)
            if (selected == null) {
                message(R.string.skill_evidence_invalid_attachment)
            } else {
                attachment = selected
                showAttachment()
            }
        }
    }

    init {
        binding.evidenceInput.setText(profile.evidence.description)
        binding.learningGoalInput.setText(profile.learningGoal)
        binding.availabilityInput.setText(profile.availability)
        binding.evidenceStatus.text = evidence?.let { item ->
            listOf(activity.getString(R.string.skill_evidence_status, item.status.label), item.reviewNote)
                .filter(String::isNotBlank).joinToString("\n")
        } ?: activity.getString(R.string.skill_evidence_not_submitted)
        binding.chooseAttachmentButton.setOnClickListener {
            pickAttachment.launch(arrayOf("image/*", "application/pdf"))
        }
        binding.removeAttachmentButton.setOnClickListener {
            attachment = null
            showAttachment()
        }
        showAttachment()
    }

    fun draft(): ProfileSetupDraft? {
        sharePicker.commitTypedSkill()
        learnPicker.commitTypedSkill()
        val sharedSkills = sharePicker.selectedSkills()
        val proof = SkillEvidenceInput(binding.evidenceInput.text.toString().trim(), attachment)
        if (sharedSkills.isEmpty() && !proof.isEmpty) {
            binding.skillsToShareInput.error = activity.getString(R.string.skill_evidence_requires_skill)
            binding.skillsToShareInput.requestFocus()
            return null
        }
        proof.attachment?.let { file ->
            if (!retainReadAccess(Uri.parse(file.uri))) {
                message(R.string.skill_evidence_attachment_unavailable)
                return null
            }
        }
        return ProfileSetupDraft(
            skillsToShare = sharedSkills,
            skillsToLearn = learnPicker.selectedSkills(),
            learningGoal = binding.learningGoalInput.text.toString().trim(),
            availability = binding.availabilityInput.text.toString().trim(),
            evidence = proof
        )
    }

    fun saveState(outState: Bundle) {
        outState.putStringArrayList(SHARED_SKILLS_STATE, ArrayList(sharePicker.selectedSkills()))
        outState.putStringArrayList(LEARNING_SKILLS_STATE, ArrayList(learnPicker.selectedSkills()))
        outState.putBundle(ATTACHMENT_STATE, Bundle().apply {
            attachment?.let { file ->
                putString("uri", file.uri)
                putString("name", file.name)
                putString("mime_type", file.mimeType)
            }
        })
    }

    private fun showAttachment() {
        val file = attachment
        binding.attachmentName.isVisible = file != null
        binding.attachmentName.text = file?.name.orEmpty()
        binding.removeAttachmentButton.isVisible = file != null
    }

    private fun readAttachment(uri: Uri): SkillEvidenceAttachment? {
        if (uri.scheme != "content") return null
        return try {
            val resolver = activity.contentResolver
            val type = resolver.getType(uri) ?: return null
            if (type != "application/pdf" && !type.startsWith("image/")) return null
            val name = resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
                ?.use { cursor -> if (cursor.moveToFirst()) cursor.getString(0) else null }
                ?: activity.getString(R.string.skill_evidence_attachment)
            SkillEvidenceAttachment(uri.toString(), name, type)
        } catch (_: SecurityException) {
            null
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    private fun retainReadAccess(uri: Uri): Boolean = try {
        val resolver = activity.contentResolver
        if (resolver.persistedUriPermissions.none { it.uri == uri && it.isReadPermission }) {
            resolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        true
    } catch (_: SecurityException) {
        false
    } catch (_: IllegalArgumentException) {
        false
    }

    private fun message(text: Int) = Toast.makeText(activity, text, Toast.LENGTH_LONG).show()

    private companion object {
        const val ATTACHMENT_STATE = "learner_skill_evidence_attachment"
        const val SHARED_SKILLS_STATE = "learner_shared_skills"
        const val LEARNING_SKILLS_STATE = "learner_learning_skills"

        fun restoreAttachment(bundle: Bundle): SkillEvidenceAttachment? {
            val uri = bundle.getString("uri") ?: return null
            return SkillEvidenceAttachment(uri, bundle.getString("name").orEmpty(), bundle.getString("mime_type").orEmpty())
        }
    }
}
