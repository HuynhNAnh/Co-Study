package com.ute.co_study.feature.learner.viewmodel.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ute.co_study.feature.learner.data.repository.ProfileSetupRepository
import com.ute.co_study.feature.learner.model.ProfileSetupDraft

class ProfileSetupViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ProfileSetupRepository(application)

    fun loadDraft(email: String): ProfileSetupDraft = repository.load(email)

    fun saveDraft(email: String, draft: ProfileSetupDraft) = repository.save(email, draft)
}
