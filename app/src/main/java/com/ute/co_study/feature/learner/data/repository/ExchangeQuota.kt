package com.ute.co_study.feature.learner.data.repository

import android.content.Context
import com.ute.co_study.shared.data.CommunityRulesRepository
import com.ute.co_study.shared.data.DemoRole
import com.ute.co_study.shared.data.DemoSession
import com.ute.co_study.shared.model.LocalStudyDay
import java.util.Locale

internal class ExchangeQuota(context: Context) {
    private val store = context.getSharedPreferences("exchange_quota", Context.MODE_PRIVATE)
    private val session = DemoSession(context)
    private val rules = CommunityRulesRepository(context)

    fun consume(): Boolean {
        if (session.role != DemoRole.LEARNER) return false
        val account = session.email.lowercase(Locale.ROOT)
        val day = LocalStudyDay.index(System.currentTimeMillis())
        val count = if (store.getLong("$account:day", -1L) == day) store.getInt("$account:count", 0) else 0
        if (count >= rules.load().dailyInvites) return false
        return store.edit().putLong("$account:day", day).putInt("$account:count", count + 1).commit()
    }
}
