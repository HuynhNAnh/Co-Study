package com.ute.co_study.shared.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

internal object LocalStudyDay {
    private const val MILLIS_PER_DAY = 86_400_000L
    private val zone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh")

    fun index(timestamp: Long): Long = Math.floorDiv(timestamp + zone.getOffset(timestamp), MILLIS_PER_DAY)

    fun format(timestamp: Long): String = SimpleDateFormat("dd/MM/yyyy", Locale.US).apply {
        timeZone = zone
    }.format(Date(timestamp))
}
