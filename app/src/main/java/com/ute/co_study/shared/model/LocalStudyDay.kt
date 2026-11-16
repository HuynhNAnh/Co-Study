package com.ute.co_study.shared.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

internal object LocalStudyDay {
    private const val MILLIS_PER_DAY = 86_400_000L
    private val zone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh")

    fun index(timestamp: Long): Long = Math.floorDiv(timestamp + zone.getOffset(timestamp), MILLIS_PER_DAY)

    fun format(timestamp: Long, daysAfter: Int = 0): String {
        val calendar = Calendar.getInstance(zone).apply {
            timeInMillis = timestamp
            add(Calendar.DAY_OF_MONTH, daysAfter)
        }
        return SimpleDateFormat("dd/MM/yyyy", Locale.US).apply {
            timeZone = zone
        }.format(calendar.time)
    }
}
