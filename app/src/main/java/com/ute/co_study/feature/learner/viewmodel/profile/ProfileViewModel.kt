package com.ute.co_study.feature.learner.viewmodel.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ute.co_study.feature.learner.data.repository.LearnerStudyRepository
import com.ute.co_study.feature.learner.data.repository.ProfileSetupRepository
import com.ute.co_study.shared.data.DemoSession
import com.ute.co_study.shared.data.ReportRepository

internal class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    val profiles = ProfileSetupRepository(application)
    val reports = ReportRepository(application)
    val study = LearnerStudyRepository(application, DemoSession(application).email)
}
