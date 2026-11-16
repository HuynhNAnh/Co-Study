package com.ute.co_study.shared.data

import android.content.Context
import java.util.Locale

internal const val STAFF_DEMO_PASSWORD = "costudy-demo"
internal const val MODERATOR_DEMO_EMAIL = "moderator@costudy.example"
internal const val MODERATOR_DEMO_CODE = "KDV-01"

enum class DemoRole { LEARNER, MODERATOR, ADMIN }

internal class DemoSession(context: Context) {
    private val preferences = context.getSharedPreferences("co_study_session", Context.MODE_PRIVATE)
    private val learners = context.getSharedPreferences("co_study_learners", Context.MODE_PRIVATE)

    val role: DemoRole?
        get() = DemoRole.entries.find { it.name == preferences.getString("role", null) }
    val email: String get() = preferences.getString("email", "").orEmpty()
    val name: String get() = preferences.getString("name", "").orEmpty()
    fun isConnected(partnerKey: String): Boolean =
        learners.getBoolean(accountKey(email, partnerKey.lowercase(Locale.ROOT)), false)

    fun start(role: DemoRole, email: String, name: String) {
        if (role == DemoRole.LEARNER) {
            learners.edit().putString(accountKey(email, "email"), email)
                .putString(accountKey(email, "name"), name).apply()
        }
        preferences.edit().putString("role", role.name).putString("email", email)
            .putString("name", name).apply()
    }

    fun learnerEmail(email: String): String? = learners.getString(accountKey(email, "email"), null)

    fun learnerName(email: String): String = learners.getString(accountKey(email, "name"), "").orEmpty()

    fun updateName(name: String) {
        if (role != DemoRole.LEARNER) return
        learners.edit().putString(accountKey(email, "name"), name).apply()
        preferences.edit().putString("name", name).apply()
    }

    fun connectWith(partnerKey: String) {
        if (role != DemoRole.LEARNER) return
        learners.edit().putBoolean(accountKey(email, partnerKey.lowercase(Locale.ROOT)), true).apply()
    }

    fun end() {
        preferences.edit().clear().apply()
    }

    private fun accountKey(email: String, field: String) = "${email.trim().lowercase(Locale.ROOT)}:$field"
}
