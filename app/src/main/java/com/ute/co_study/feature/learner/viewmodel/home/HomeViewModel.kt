package com.ute.co_study.feature.learner.viewmodel.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ute.co_study.feature.learner.data.repository.ProfileSetupRepository
import com.ute.co_study.feature.learner.model.ProfileSetupDraft

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ProfileSetupRepository(application)

    fun loadProfile(email: String): ProfileSetupDraft = repository.load(email)
}
