package com.ute.co_study.feature.learner.viewmodel.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ute.co_study.feature.learner.data.repository.ProfileSetupRepository
import com.ute.co_study.feature.learner.model.ProfileSetupDraft
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileSetupViewModel(application: Application) : AndroidViewModel(application) {
    enum class SaveState { IDLE, SAVING, SAVED, FAILED }

    private val repository = ProfileSetupRepository(application)
    private val mutableSaveState = MutableStateFlow(SaveState.IDLE)
    val saveState = mutableSaveState.asStateFlow()

    fun loadDraft(email: String): ProfileSetupDraft = repository.load(email)

    fun saveDraft(email: String, draft: ProfileSetupDraft) {
        if (mutableSaveState.value != SaveState.IDLE) return

        mutableSaveState.value = SaveState.SAVING
        viewModelScope.launch {
            val saved = withContext(Dispatchers.IO) { repository.save(email, draft) }
            mutableSaveState.value = if (saved) SaveState.SAVED else SaveState.FAILED
        }
    }

    fun acknowledgeSaveFailure() {
        if (mutableSaveState.value == SaveState.FAILED) {
            mutableSaveState.value = SaveState.IDLE
        }
    }
}
