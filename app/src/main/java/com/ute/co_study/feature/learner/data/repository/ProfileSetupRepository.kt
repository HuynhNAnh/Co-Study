package com.ute.co_study.feature.learner.data.repository

import android.content.Context
import com.ute.co_study.feature.learner.model.ProfileSetupDraft
import org.json.JSONException
import org.json.JSONObject

internal class ProfileSetupRepository(context: Context) {
    private val drafts = context.getSharedPreferences("profile_setup_drafts", Context.MODE_PRIVATE)

    fun hasDraft(email: String) = drafts.contains(email)

    fun load(email: String): ProfileSetupDraft {
        val rawDraft = drafts.getString(email, null) ?: return ProfileSetupDraft()
        val draft = try {
            JSONObject(rawDraft)
        } catch (_: JSONException) {
            return ProfileSetupDraft()
        }
        return ProfileSetupDraft(
            skillsToShare = draft.optString(KEY_SKILLS_TO_SHARE),
            skillsToLearn = draft.optString(KEY_SKILLS_TO_LEARN),
            learningGoal = draft.optString(KEY_LEARNING_GOAL),
            availability = draft.optString(KEY_AVAILABILITY)
        )
    }

    fun save(email: String, draft: ProfileSetupDraft): Boolean {
        val json = JSONObject()
            .put(KEY_SKILLS_TO_SHARE, draft.skillsToShare)
            .put(KEY_SKILLS_TO_LEARN, draft.skillsToLearn)
            .put(KEY_LEARNING_GOAL, draft.learningGoal)
            .put(KEY_AVAILABILITY, draft.availability)
        return drafts.edit().putString(email, json.toString()).commit()
    }

    private companion object {
        const val KEY_SKILLS_TO_SHARE = "skills_to_share"
        const val KEY_SKILLS_TO_LEARN = "skills_to_learn"
        const val KEY_LEARNING_GOAL = "learning_goal"
        const val KEY_AVAILABILITY = "availability"
    }
}
