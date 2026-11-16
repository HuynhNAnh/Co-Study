package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudyConversation
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged

class MessagesActivity : StudyActivity(R.layout.activity_messages) {
    override val refreshOnReturn = true

    override fun render() {
        val search = input("Tìm bạn hoặc nhóm học", hint = "Tên bạn học", maxLength = 80)
        val results = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        body.addView(results, LinearLayout.LayoutParams(-1, -2))
        val rows = StudyConversation.entries.map { conversation ->
            val preview = study.messages(conversation).lastOrNull()?.text.orEmpty()
            conversation to person(conversation.initials, conversation.title, preview, results) {
                openConversation(conversation)
            }
        }
        val empty = label("Không tìm thấy cuộc trò chuyện", results)
        fun filter(query: String) {
            rows.forEach { (conversation, view) ->
                view.isVisible = conversation.title.contains(query, ignoreCase = true)
            }
            empty.isVisible = rows.none { (_, view) -> view.isVisible }
        }
        filter(search.text.toString())
        search.doAfterTextChanged { filter(it.toString().trim()) }
        card("Học tốt hơn khi có bạn đồng hành", "Trao đổi mục tiêu và chọn thời gian phù hợp trước mỗi buổi học.", Tone.MINT)
        learnerTabs(2)
    }
}
