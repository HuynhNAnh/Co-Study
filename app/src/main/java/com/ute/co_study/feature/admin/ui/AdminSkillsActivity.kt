package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R

class AdminSkillsActivity : AdminActivity(R.layout.activity_admin_skills) {
    override val refreshOnReturn = true

    override fun render() {
        val skills = admin.skills()
        val groups = skills.groupBy { it.group }
        status("${skills.size} kỹ năng · ${groups.size} nhóm")
        groups.forEach { (group, items) ->
            label(group.uppercase())
            items.forEach { skill ->
                row(skill.name, "${skill.id} · ${skill.status}") { openSkill(AdminSkillEditActivity::class.java, skill) }
            }
        }
        if (skills.isEmpty()) note("Danh mục kỹ năng đang trống.")
        adminTabs(3)
    }
}
