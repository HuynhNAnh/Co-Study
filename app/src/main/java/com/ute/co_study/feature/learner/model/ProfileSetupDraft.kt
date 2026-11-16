package com.ute.co_study.feature.learner.model

data class ProfileSetupDraft(
    val skillsToShare: String = "",
    val skillsToLearn: String = "",
    val learningGoal: String = "",
    val availability: String = ""
)
