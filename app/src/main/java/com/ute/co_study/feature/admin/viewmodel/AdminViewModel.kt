package com.ute.co_study.feature.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/** Holds the current Admin screen and navigation history across activity recreation. */
class AdminViewModel : ViewModel() {
    private val history = ArrayDeque<Int>()
    private val mutableScreenId = MutableLiveData(0)

    val screenId: LiveData<Int> = mutableScreenId

    fun navigate(destination: Int, addToHistory: Boolean = true) {
        val current = mutableScreenId.value ?: 0
        if (current == destination) return
        if (addToHistory) history.addLast(current)
        mutableScreenId.value = destination
    }

    fun navigateBack(fallback: Int) {
        val matchingIndex = history.indexOfLast { it == fallback }
        if (matchingIndex >= 0) {
            while (history.size > matchingIndex + 1) history.removeLast()
            history.removeLast()
        }
        mutableScreenId.value = fallback
    }

    /** Returns false when the activity should finish instead of navigating. */
    fun handleSystemBack(): Boolean {
        if (history.isNotEmpty()) {
            mutableScreenId.value = history.removeLast()
            return true
        }

        return when (mutableScreenId.value) {
            0, 1 -> false
            2, 12, 16, 19 -> {
                navigate(1, addToHistory = false)
                true
            }
            else -> {
                navigate(1, addToHistory = false)
                true
            }
        }
    }
}
