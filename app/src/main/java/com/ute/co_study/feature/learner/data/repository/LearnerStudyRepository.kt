package com.ute.co_study.feature.learner.data.repository

import android.content.Context
import com.ute.co_study.feature.learner.model.StudyConversation
import com.ute.co_study.feature.learner.model.StudyMessage
import com.ute.co_study.feature.learner.model.StudySession
import com.ute.co_study.feature.learner.model.StudySlot
import com.ute.co_study.feature.learner.model.StudyStatus
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

internal class LearnerStudyRepository(context: Context, email: String) {
    private val store = context.getSharedPreferences("learner_study", Context.MODE_PRIVATE)
    private val account = email.trim().lowercase(Locale.ROOT)

    fun messages(conversation: StudyConversation): List<StudyMessage> {
        val saved = JSONArray(store.getString(messageKey(conversation), "[]") ?: "[]")
        return conversation.initialMessages + List(saved.length()) {
            StudyMessage(saved.getString(it), true)
        }
    }

    fun send(conversation: StudyConversation, text: String) {
        val key = messageKey(conversation)
        val sent = JSONArray(store.getString(key, "[]") ?: "[]").put(text)
        store.edit().putString(key, sent.toString()).apply()
    }

    fun sessions(): List<StudySession> {
        val saved = store.getString("$account:sessions", null)
        if (saved == null) return listOf(sampleSession)
        val rows = JSONArray(saved)
        return List(rows.length()) { index -> rows.getJSONObject(index).toSession() }
    }

    fun find(id: Long): StudySession? = sessions().firstOrNull { it.id == id }

    fun propose(partner: String, subject: String, slot: StudySlot, mode: String): StudySession {
        val previous = sessions()
        val id = (previous.maxOfOrNull { it.id } ?: 0) + 1
        val next = StudySession(id, partner, subject, slot, mode, StudyStatus.PENDING)
        save(previous + next)
        return next
    }

    fun confirm(id: Long): StudySession? = change(id, setOf(StudyStatus.PENDING, StudyStatus.RESCHEDULING)) { item ->
        if (item.status == StudyStatus.PENDING) item.copy(status = StudyStatus.CONFIRMED)
        else item.copy(status = StudyStatus.RESCHEDULED, slot = checkNotNull(item.newSlot), newSlot = null)
    }

    fun reschedule(id: Long, slot: StudySlot, reason: String): StudySession? =
        change(id, confirmed) { it.copy(status = StudyStatus.RESCHEDULING, newSlot = slot, reason = reason) }

    fun cancel(id: Long, reason: String): StudySession? =
        change(id, confirmed + StudyStatus.RESCHEDULING) {
            it.copy(status = StudyStatus.CANCELED, newSlot = null, reason = reason)
        }

    fun complete(id: Long): StudySession? = change(id, confirmed) {
        it.copy(status = StudyStatus.COMPLETED, completedAt = System.currentTimeMillis())
    }

    fun confirmAttendance(id: Long): StudySession? {
        val item = find(id)?.takeIf { it.partnerConfirmedAt == 0L } ?: return null
        val now = System.currentTimeMillis()
        if (now - item.completedAt !in 0..48L * 60 * 60 * 1000) return null
        return change(id, setOf(StudyStatus.COMPLETED)) { it.copy(partnerConfirmedAt = now) }
    }

    fun review(id: Long, stars: Int, review: String): StudySession? {
        if (stars !in 1..5 || find(id)?.stars != 0) return null
        return change(id, setOf(StudyStatus.COMPLETED)) { it.copy(stars = stars, review = review) }
    }

    private fun change(id: Long, allowed: Set<StudyStatus>, update: (StudySession) -> StudySession): StudySession? {
        val sessions = sessions()
        val current = sessions.firstOrNull { it.id == id && it.status in allowed } ?: return null
        val changed = update(current)
        save(sessions.map { if (it.id == id) changed else it })
        return changed
    }

    private fun save(sessions: List<StudySession>) {
        val rows = JSONArray()
        sessions.forEach { item ->
            rows.put(JSONObject()
                .put("id", item.id).put("partner", item.partner)
                .put("subject", item.subject).put("slot", item.slot.toJson()).put("mode", item.mode)
                .put("status", item.status.name).put("new_slot", item.newSlot?.toJson())
                .put("reason", item.reason).put("stars", item.stars).put("review", item.review)
                .put("completed_at", item.completedAt).put("partner_confirmed_at", item.partnerConfirmedAt))
        }
        store.edit().putString("$account:sessions", rows.toString()).apply()
    }

    private fun JSONObject.toSession() = StudySession(
        id = getLong("id"), partner = getString("partner"), subject = getString("subject"),
        slot = getJSONObject("slot").toSlot(), mode = getString("mode"),
        status = StudyStatus.valueOf(getString("status")), newSlot = optJSONObject("new_slot")?.toSlot(),
        reason = optString("reason"), stars = optInt("stars"), review = optString("review"),
        completedAt = optLong("completed_at"), partnerConfirmedAt = optLong("partner_confirmed_at")
    )

    private fun StudySlot.toJson() = JSONObject().put("date", date).put("start", start).put("end", end)
    private fun JSONObject.toSlot() = StudySlot(getString("date"), getString("start"), getString("end"))
    private fun messageKey(conversation: StudyConversation) = "$account:messages:${conversation.name}"

    private companion object {
        val confirmed = setOf(StudyStatus.CONFIRMED, StudyStatus.RESCHEDULED)
        val sampleSession = StudySession(
            1L, "Minh Anh", "Kotlin Activity & Intent",
            StudySlot("08/10/2026", "19:00", "20:00"), "Trực tuyến · Google Meet",
            StudyStatus.CONFIRMED
        )
    }
}
