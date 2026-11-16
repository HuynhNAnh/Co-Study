package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R

class AdminJournalActivity : AdminActivity(R.layout.activity_admin_journal) {
    override val refreshOnReturn = true

    override fun render() {
        val events = admin.journal()
        events.forEach { item -> card(item.title, "${item.at} · ${item.actor}\n${item.detail}") }
        if (events.isEmpty()) note("Chưa có hoạt động quản trị được ghi nhận.")
        button("Về hệ thống", Tone.PLAIN) { returnTo(AdminSystemActivity::class.java) }
    }
}
