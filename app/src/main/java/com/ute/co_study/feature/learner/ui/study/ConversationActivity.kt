package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import android.widget.TextView

class ConversationActivity : StudyActivity(R.layout.activity_conversation) {
    override val refreshOnReturn = true

    override fun render() {
        val conversation = conversation()
        findViewById<TextView>(R.id.workflow_title).text = conversation.title
        status(conversation.subtitle)
        label("Cuộc trò chuyện · Dữ liệu minh họa")
        study.messages(conversation).forEach { bubble(it.text, it.sentByLearner) }
        study.sessions().filter { it.partner == conversation.title }.asReversed().forEach { item ->
            row(item.subject, "${item.status.label} · ${item.slot.display}") { openDetails(item) }
        }
        if (!conversation.isGroup) {
            button("Đề xuất lịch học", parent = body) {
                open(ProposeStudyActivity::class.java, conversation.arguments())
            }
        }
        val composer = input("Tin nhắn", hint = "Nhập tin nhắn…", multiline = true, parent = actions)
        button("Gửi tin nhắn") {
            val value = required(composer) ?: return@button
            study.send(conversation, value)
            refresh()
            revealLatest()
        }
    }
}
