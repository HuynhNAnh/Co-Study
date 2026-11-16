package com.ute.co_study.shared.data

import android.content.Context

enum class DemoRole { LEARNER, MODERATOR, ADMIN }

internal class DemoSession(context: Context) {
    private val preferences = context.getSharedPreferences("co_study_session", Context.MODE_PRIVATE)

    val role: DemoRole?
        get() = DemoRole.entries.find { it.name == preferences.getString("role", null) }
    val email: String get() = preferences.getString("email", "").orEmpty()
    val name: String get() = preferences.getString("name", "").orEmpty()

    fun start(role: DemoRole, email: String, name: String) {
        preferences.edit().putString("role", role.name).putString("email", email)
            .putString("name", name).apply()
    }

    fun updateName(name: String) {
        preferences.edit().putString("name", name).apply()
    }

    fun end() {
        preferences.edit().clear().apply()
    }
}
