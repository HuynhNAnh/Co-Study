package com.ute.co_study.feature.admin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ute.co_study.feature.admin.data.repository.AdminRepository

internal class AdminViewModel(application: Application) : AndroidViewModel(application) {
    val repository = AdminRepository(application)
}
