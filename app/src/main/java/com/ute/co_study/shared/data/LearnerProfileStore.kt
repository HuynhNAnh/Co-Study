package com.ute.co_study.shared.data

import android.content.Context
import org.json.JSONException
import org.json.JSONObject

internal class LearnerProfileStore(context: Context) {
    private val preferences = context.getSharedPreferences("profile_setup_drafts", Context.MODE_PRIVATE)

    fun contains(email: String): Boolean = preferences.contains(email)

    fun read(email: String): JSONObject = parse(preferences.getString(email, null)) ?: JSONObject()

    fun entries(): List<Pair<String, JSONObject>> =
        preferences.all.mapNotNull { (email, rawProfile) -> parse(rawProfile as? String)?.let { email to it } }

    fun update(email: String, change: (JSONObject) -> Boolean): Boolean = synchronized(writeLock) {
        val profile = read(email)
        if (!change(profile)) return@synchronized false
        preferences.edit().putString(email, profile.toString()).commit()
    }

    private fun parse(rawProfile: String?): JSONObject? =
        if (rawProfile == null) null else try {
            JSONObject(rawProfile)
        } catch (_: JSONException) {
            null
        }

    private companion object {
        val writeLock = Any()
    }
}
