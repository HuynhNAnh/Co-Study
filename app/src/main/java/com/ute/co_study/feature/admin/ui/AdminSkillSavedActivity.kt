package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R

class AdminSkillSavedActivity : AdminActivity(R.layout.activity_admin_skill_saved) {
    override fun render() {
        val skill = skill() ?: return
        result("Đã cập nhật ${skill.id}", "${skill.name}\nNhóm: ${skill.group} · ${skill.status}")
        card("Mô tả mới", skill.description)
        button("Về danh mục") { returnTo(AdminSkillsActivity::class.java) }
    }
}
