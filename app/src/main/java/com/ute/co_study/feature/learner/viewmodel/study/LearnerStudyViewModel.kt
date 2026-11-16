package com.ute.co_study.feature.learner.viewmodel.study

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ute.co_study.feature.learner.data.repository.LearnerStudyRepository
import com.ute.co_study.shared.data.DemoSession

internal class LearnerStudyViewModel(application: Application) : AndroidViewModel(application) {
    val study = LearnerStudyRepository(application, DemoSession(application).email)
}
