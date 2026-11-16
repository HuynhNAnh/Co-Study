package com.ute.co_study.shared.data

import android.content.Context
import android.content.SharedPreferences
import com.ute.co_study.shared.model.ManagedSkill
import org.json.JSONArray
import org.json.JSONObject

internal class SkillCatalog(context: Context) {
    private val store = communityPreferences(context)

    fun visibleNames(): List<String> = load().filter { it.visible }.map { it.name }.distinct()
        .sortedWith(String.CASE_INSENSITIVE_ORDER)

    fun load(): List<ManagedSkill> {
        val saved = store.getString(SKILLS, null) ?: return sampleSkills
        val items = JSONArray(saved)
        return List(items.length()) { index ->
            val data = items.getJSONObject(index)
            ManagedSkill(
                data.getString("id"), data.getString("name"), data.getString("group"),
                data.getString("description"), data.getBoolean("visible")
            )
        }
    }

    fun writeTo(editor: SharedPreferences.Editor, skills: List<ManagedSkill>) {
        val rows = JSONArray()
        skills.forEach { skill ->
            rows.put(JSONObject().put("id", skill.id).put("name", skill.name).put("group", skill.group)
                .put("description", skill.description).put("visible", skill.visible))
        }
        editor.putString(SKILLS, rows.toString())
    }

    private companion object {
        const val SKILLS = "admin_skills"
        val sampleSkills = listOf(
            ManagedSkill("SK-001", "Kotlin / Android", "Lập trình", "Lập trình Android bằng Kotlin, Activity, Intent và ViewBinding.", true),
            ManagedSkill("SK-002", "Figma", "Thiết kế", "Thiết kế và dựng mẫu giao diện.", true),
            ManagedSkill("SK-003", "Tiếng Anh", "Ngôn ngữ", "Luyện giao tiếp và đọc tài liệu.", true),
            ManagedSkill("SK-004", "Tiếng Nhật", "Ngôn ngữ", "Từ vựng và hội thoại cơ bản.", true)
        )
    }
}
