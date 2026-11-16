package com.ute.co_study.feature.learner.ui.profile

import androidx.activity.viewModels
import com.ute.co_study.feature.learner.ui.LearnerActivity
import com.ute.co_study.feature.learner.viewmodel.profile.ProfileViewModel

abstract class ProfileActivity(layout: Int) : LearnerActivity(layout) {
    private val model: ProfileViewModel by viewModels()
    internal val profiles get() = model.profiles
    internal val reports get() = model.reports
    internal val study get() = model.study
    protected val displayName get() = session.name.ifBlank { session.email.substringBefore('@') }

    protected fun identity(detail: String = session.email) {
        val initials = displayName.split(Regex("\\s+")).takeLast(2).joinToString("") { it.take(1) }.uppercase()
        person(initials, displayName, detail)
    }

    protected fun reputationMetrics() {
        metrics("92/100" to "Uy tín mẫu", "4,8/5" to "Điểm mẫu", study.sessions().count { it.completedAt != 0L }.toString() to "Buổi đã học")
    }

    protected fun value(text: String) = text.ifBlank { "Chưa cập nhật" }

    internal companion object {
        const val REPORT_ID = "learner_report_id"
    }
}
