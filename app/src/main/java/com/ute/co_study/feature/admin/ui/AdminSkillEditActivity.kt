package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R

class AdminSkillEditActivity : AdminActivity(R.layout.activity_admin_skill_edit) {
    override fun render() {
        val skill = skill() ?: return
        status("${skill.id} · ${skill.name}")
        val name = input("Tên kỹ năng", skill.name, maxLength = 100)
        val group = input("Nhóm kỹ năng", skill.group, maxLength = 100)
        val description = input("Mô tả kỹ năng", skill.description, multiline = true)
        val visibility = choice("Trạng thái", listOf("Đang hiển thị", "Đã ẩn"), skill.status)
        note("Mã ${skill.id} được giữ lại để tra cứu lịch sử thay đổi.")
        button("Lưu thay đổi") {
            val skillName = required(name) ?: return@button
            val skillGroup = required(group) ?: return@button
            val detail = required(description) ?: return@button
            val updated = skill.copy(name = skillName, group = skillGroup, description = detail, visible = visibility.text == "Đang hiển thị")
            if (admin.saveSkill(updated)) {
                openSkill(AdminSkillSavedActivity::class.java, updated)
                finish()
            } else actionFailed()
        }
        button("Hủy chỉnh sửa", Tone.PLAIN) { finish() }
    }
}
