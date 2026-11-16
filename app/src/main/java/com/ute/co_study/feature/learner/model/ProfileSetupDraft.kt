package com.ute.co_study.feature.learner.model

import com.ute.co_study.shared.model.SkillEvidenceInput

data class ProfileSetupDraft(
    val skillsToShare: List<String> = emptyList(),
    val skillsToLearn: List<String> = emptyList(),
    val learningGoal: String = "",
    val availability: String = "",
    val evidence: SkillEvidenceInput = SkillEvidenceInput()
)
