package com.ute.co_study.shared.data

import android.content.Context
import android.content.SharedPreferences
import com.ute.co_study.shared.model.ManagedAccount
import org.json.JSONObject

internal class DemoMemberDirectory(context: Context) {
    private val store = communityPreferences(context)

    fun all(): List<ManagedAccount> = samples.map { sample ->
        val saved = store.getString("member:${sample.id}", null)
        if (saved == null) sample else {
            val data = JSONObject(saved)
            sample.copy(role = DemoRole.valueOf(data.getString("role")),
                lockedUntil = data.optLong("locked_until"), lockReason = data.optString("reason"))
        }
    }

    fun findByEmail(email: String): ManagedAccount? = all().firstOrNull { it.email.equals(email, ignoreCase = true) }

    fun writeTo(editor: SharedPreferences.Editor, account: ManagedAccount) {
        editor.putString("member:${account.id}", JSONObject()
            .put("role", account.role.name).put("locked_until", account.lockedUntil)
            .put("reason", account.lockReason).toString())
    }

    private companion object {
        val samples = listOf(
            ManagedAccount("TV-024", "Thành Đạt", "thanhdat@costudy.example", DemoRole.LEARNER),
            ManagedAccount("LC-014", "Lan Chi", "lanchi@costudy.example", DemoRole.LEARNER)
        )
    }
}
