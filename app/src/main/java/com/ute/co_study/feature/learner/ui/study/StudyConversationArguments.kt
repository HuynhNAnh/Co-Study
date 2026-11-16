package com.ute.co_study.feature.learner.ui.study

import android.content.Intent
import android.os.Bundle
import com.ute.co_study.feature.learner.model.StudyConversation

private const val CONVERSATION = "study_conversation"

internal fun StudyConversation.arguments(): Bundle = Bundle().apply { putString(CONVERSATION, name) }

internal fun Intent.studyConversation(): StudyConversation {
    val key = getStringExtra(CONVERSATION)
    return StudyConversation.entries.firstOrNull { it.name == key } ?: StudyConversation.MINH_ANH
}
