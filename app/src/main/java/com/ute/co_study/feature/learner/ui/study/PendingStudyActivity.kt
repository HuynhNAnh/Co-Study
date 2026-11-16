package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudyStatus

class PendingStudyActivity : StudyActivity(R.layout.activity_pending_study) {
    override fun render() {
        val item = expect(StudyStatus.PENDING) ?: return
        partnerCard(item.partner)
        sessionCard(item)
        note("Bạn có thể tiếp tục trao đổi trong cuộc trò chuyện.")
        button("Mô phỏng bạn học đồng ý") { changed(study.confirm(item.id), StudyDetailsActivity::class.java) }
        button("Về cuộc trò chuyện", Tone.PLAIN) { openConversationFor(item); finish() }
    }
}
