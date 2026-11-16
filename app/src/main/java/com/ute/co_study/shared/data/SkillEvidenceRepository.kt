package com.ute.co_study.shared.data

import android.content.Context
import com.ute.co_study.shared.model.SkillEvidence
import com.ute.co_study.shared.model.SkillEvidenceAttachment
import com.ute.co_study.shared.model.SkillEvidenceInput
import com.ute.co_study.shared.model.SkillEvidenceStatus
import org.json.JSONObject
import java.util.UUID

internal class SkillEvidenceRepository(context: Context) {
    private val session = DemoSession(context)
    private val profiles = LearnerProfileStore(context)

    fun queue(): List<SkillEvidence> {
        if (session.role != DemoRole.MODERATOR) return emptyList()
        return profiles.entries().mapNotNull { (email, profile) -> read(profile, email) }
            .sortedWith(
                compareBy<SkillEvidence> { it.status != SkillEvidenceStatus.PENDING }
                    .thenByDescending { maxOf(it.submittedAt, it.reviewedAt) }
            )
    }

    fun find(id: String): SkillEvidence? = queue().firstOrNull { it.id == id }

    fun review(id: String, status: SkillEvidenceStatus, note: String): Boolean {
        if (status == SkillEvidenceStatus.PENDING ||
            (status == SkillEvidenceStatus.REJECTED && note.isBlank())
        ) return false

        val item = find(id) ?: return false
        return profiles.update(item.ownerEmail) { profile ->
            val current = read(profile, item.ownerEmail) ?: return@update false
            if (session.role != DemoRole.MODERATOR ||
                current.id != id || current.status != SkillEvidenceStatus.PENDING
            ) return@update false

            write(profile, current.copy(
                status = status,
                reviewer = session.name.ifBlank { session.email },
                reviewNote = note.trim(),
                reviewedAt = System.currentTimeMillis()
            ))
            true
        }
    }

    companion object {
        private const val EVIDENCE = "skill_evidence"

        fun read(profile: JSONObject, email: String): SkillEvidence? {
            val row = profile.optJSONObject(EVIDENCE) ?: return null
            val status = SkillEvidenceStatus.entries.firstOrNull { it.name == row.optString("status") } ?: return null
            val id = row.optString("id").takeIf(String::isNotBlank) ?: return null
            val attachment = row.optJSONObject("attachment")?.let {
                SkillEvidenceAttachment(it.optString("uri"), it.optString("name"), it.optString("mime_type"))
            }
            return SkillEvidence(
                id = id,
                ownerEmail = email,
                ownerName = row.optString("owner_name").ifBlank { email },
                skill = row.optString("skill"),
                input = SkillEvidenceInput(row.optString("description"), attachment),
                status = status,
                submittedAt = row.optLong("submitted_at"),
                reviewer = row.optString("reviewer"),
                reviewNote = row.optString("review_note"),
                reviewedAt = row.optLong("reviewed_at")
            )
        }

        fun updateSubmission(
            profile: JSONObject,
            email: String,
            learnerName: String,
            skill: String,
            input: SkillEvidenceInput
        ) {
            if (input.isEmpty) {
                profile.remove(EVIDENCE)
                return
            }

            val previous = read(profile, email)
            val name = learnerName.ifBlank { email }
            val evidence = if (previous != null && previous.skill == skill && previous.input == input) {
                previous.copy(ownerName = name)
            } else {
                SkillEvidence(
                    id = UUID.randomUUID().toString(),
                    ownerEmail = email,
                    ownerName = name,
                    skill = skill,
                    input = input,
                    status = SkillEvidenceStatus.PENDING,
                    submittedAt = System.currentTimeMillis()
                )
            }
            write(profile, evidence)
        }

        private fun write(profile: JSONObject, evidence: SkillEvidence) {
            val attachment = evidence.input.attachment?.let {
                JSONObject().put("uri", it.uri).put("name", it.name).put("mime_type", it.mimeType)
            }
            profile.put(EVIDENCE, JSONObject()
                .put("id", evidence.id)
                .put("owner_name", evidence.ownerName)
                .put("skill", evidence.skill)
                .put("description", evidence.input.description)
                .put("attachment", attachment)
                .put("status", evidence.status.name)
                .put("submitted_at", evidence.submittedAt)
                .put("reviewer", evidence.reviewer)
                .put("review_note", evidence.reviewNote)
                .put("reviewed_at", evidence.reviewedAt))
        }
    }
}
