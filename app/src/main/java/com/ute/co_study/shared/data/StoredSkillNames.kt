package com.ute.co_study.shared.data

import org.json.JSONObject
import java.util.Locale

internal fun JSONObject.readSkillNames(key: String, legacyKey: String = key): List<String> {
    val values = optJSONArray(key)
    val names = if (values == null) {
        listOfNotNull(opt(legacyKey) as? String)
    } else {
        (0 until values.length()).mapNotNull { values.opt(it) as? String }
    }
    return names.map(String::trim).filter(String::isNotEmpty).distinctBy { it.lowercase(Locale.ROOT) }
}
