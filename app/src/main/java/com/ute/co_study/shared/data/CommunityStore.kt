package com.ute.co_study.shared.data

import android.content.Context
import android.content.SharedPreferences
import com.ute.co_study.shared.model.CommunityRules
import org.json.JSONArray
import org.json.JSONObject

internal fun communityPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences("community_reports", Context.MODE_PRIVATE)

internal class CommunityRulesRepository(context: Context) {
    private val preferences = communityPreferences(context)

    fun load(): CommunityRules {
        val saved = preferences.getString(RULES_KEY, null) ?: return CommunityRules()
        val rules = JSONObject(saved)
        val default = CommunityRules()
        val badges = rules.optJSONArray("badge_days")
        return CommunityRules(
            rules.optInt("daily_invites", default.dailyInvites),
            rules.optInt("report_hours", default.reportHours),
            rules.optInt("study_minutes", default.studyMinutes),
            rules.optInt("study_confirm_hours", default.studyConfirmHours),
            if (badges == null) default.badgeDays else List(badges.length()) { badges.getInt(it) },
            rules.optInt("partner_sessions", default.partnerSessions)
        )
    }

    fun writeTo(editor: SharedPreferences.Editor, rules: CommunityRules) {
        editor.putString(RULES_KEY, JSONObject()
            .put("daily_invites", rules.dailyInvites).put("report_hours", rules.reportHours)
            .put("study_minutes", rules.studyMinutes).put("study_confirm_hours", rules.studyConfirmHours)
            .put("badge_days", JSONArray(rules.badgeDays)).put("partner_sessions", rules.partnerSessions).toString())
    }

    private companion object {
        const val RULES_KEY = "community_rules"
    }
}
