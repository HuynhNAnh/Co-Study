package com.ute.co_study.feature.learner.data.repository

import android.content.Context
import com.ute.co_study.feature.learner.model.ProfileSetupDraft
import com.ute.co_study.shared.data.LearnerProfileStore
import com.ute.co_study.shared.data.SkillEvidenceRepository
import com.ute.co_study.shared.data.readSkillNames
import com.ute.co_study.shared.model.SkillEvidence
import com.ute.co_study.shared.model.SkillEvidenceInput
import org.json.JSONArray

internal class ProfileSetupRepository(context: Context) {
    private val profiles = LearnerProfileStore(context)

    fun hasDraft(email: String) = profiles.contains(email)

    fun load(email: String): ProfileSetupDraft {
        val profile = profiles.read(email)
        return ProfileSetupDraft(
            skillsToShare = profile.readSkillNames(KEY_SKILLS_TO_SHARE),
            skillsToLearn = profile.readSkillNames(KEY_SKILLS_TO_LEARN),
            learningGoal = profile.optString(KEY_LEARNING_GOAL),
            availability = profile.optString(KEY_AVAILABILITY),
            evidence = SkillEvidenceRepository.read(profile, email)?.input ?: SkillEvidenceInput()
        )
    }

    fun evidence(email: String): SkillEvidence? = SkillEvidenceRepository.read(profiles.read(email), email)

    fun save(email: String, learnerName: String, draft: ProfileSetupDraft): Boolean {
        if (email.isBlank() || (draft.skillsToShare.isEmpty() && !draft.evidence.isEmpty)) return false
        return profiles.update(email) { profile ->
            profile.put(KEY_SKILLS_TO_SHARE, JSONArray(draft.skillsToShare))
                .put(KEY_SKILLS_TO_LEARN, JSONArray(draft.skillsToLearn))
                .put(KEY_LEARNING_GOAL, draft.learningGoal)
                .put(KEY_AVAILABILITY, draft.availability)
            SkillEvidenceRepository.updateSubmission(profile, email, learnerName, draft.skillsToShare, draft.evidence)
            true
        }
    }

    private companion object {
        const val KEY_SKILLS_TO_SHARE = "skills_to_share"
        const val KEY_SKILLS_TO_LEARN = "skills_to_learn"
        const val KEY_LEARNING_GOAL = "learning_goal"
        const val KEY_AVAILABILITY = "availability"
    }
}
