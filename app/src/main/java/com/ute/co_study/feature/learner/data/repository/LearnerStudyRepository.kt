package com.ute.co_study.feature.learner.data.repository

import android.content.Context
import com.ute.co_study.feature.learner.model.LearningBadge
import com.ute.co_study.feature.learner.model.LearningProgress
import com.ute.co_study.feature.learner.model.SampleStudySchedule
import com.ute.co_study.shared.data.CommunityRulesRepository
import com.ute.co_study.shared.model.StudyRecognitionPolicy
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
    private val rules = CommunityRulesRepository(context)
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
        val saved = store.getString("$account:sessions", "[]") ?: "[]"
        val rows = JSONArray(saved)
        val sessions = List(rows.length()) { index -> rows.getJSONObject(index).toSession() }
        val presentIds = sessions.mapTo(mutableSetOf()) { it.id }
        val missing = SampleStudySchedule.missingFrom(presentIds, System.currentTimeMillis())
        if (missing.isEmpty()) return sessions
        return (sessions + missing).also(::save)
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
        it.copy(status = StudyStatus.COMPLETED, completedAt = System.currentTimeMillis(), policy = StudyRecognitionPolicy.from(rules.load()))
    }

    fun confirmAttendance(id: Long): StudySession? {
        val item = find(id) ?: return null
        val now = System.currentTimeMillis()
        if (!item.canConfirm(now)) return null
        return change(id, setOf(StudyStatus.COMPLETED)) { it.copy(partnerConfirmedAt = now) }
    }

    fun policy(): StudyRecognitionPolicy = StudyRecognitionPolicy.from(rules.load())

    fun progress(): LearningProgress = LearningProgress(
        sessions().filter { it.status == StudyStatus.COMPLETED }.sortedByDescending { it.completedAt },
        policy(), System.currentTimeMillis()
    )

    fun featuredBadge(): LearningBadge? {
        val key = store.getString("$account:featured_badge", null) ?: return null
        return progress().badges.firstOrNull { it.key == key && it.earned }
    }

    fun featureBadge(key: String?) {
        if (key == null || progress().badges.any { it.key == key && it.earned }) {
            store.edit().putString("$account:featured_badge", key).apply()
        }
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
                .put("completed_at", item.completedAt).put("partner_confirmed_at", item.partnerConfirmedAt)
                .put("policy", item.policy.toJson()))
        }
        store.edit().putString("$account:sessions", rows.toString()).apply()
    }

    private fun JSONObject.toSession() = StudySession(
        id = getLong("id"), partner = getString("partner"), subject = getString("subject"),
        slot = getJSONObject("slot").toSlot(), mode = getString("mode"),
        status = StudyStatus.valueOf(getString("status")), newSlot = optJSONObject("new_slot")?.toSlot(),
        reason = optString("reason"), stars = optInt("stars"), review = optString("review"),
        completedAt = optLong("completed_at"), partnerConfirmedAt = optLong("partner_confirmed_at"),
        policy = optJSONObject("policy")?.toPolicy() ?: StudyRecognitionPolicy()
    )

    private fun StudyRecognitionPolicy.toJson() = JSONObject().put("minutes", minimumMinutes)
        .put("hours", confirmationHours).put("streak", JSONArray(streakThresholds)).put("partner", partnerThreshold)

    private fun JSONObject.toPolicy(): StudyRecognitionPolicy {
        val streak = getJSONArray("streak")
        return StudyRecognitionPolicy(getInt("minutes"), getInt("hours"), List(streak.length()) { streak.getInt(it) }, getInt("partner"))
    }

    private fun StudySlot.toJson() = JSONObject().put("date", date).put("start", start).put("end", end)
    private fun JSONObject.toSlot() = StudySlot(getString("date"), getString("start"), getString("end"))
    private fun messageKey(conversation: StudyConversation) = "$account:messages:${conversation.name}"

    private companion object {
        val confirmed = setOf(StudyStatus.CONFIRMED, StudyStatus.RESCHEDULED)
    }
}
