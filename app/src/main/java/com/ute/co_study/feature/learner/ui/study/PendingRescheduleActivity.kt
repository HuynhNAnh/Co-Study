package com.ute.co_study.feature.learner.ui.study

import com.ute.co_study.R
import com.ute.co_study.feature.learner.model.StudyStatus

class PendingRescheduleActivity : StudyActivity(R.layout.activity_pending_reschedule) {
    override fun render() {
        val item = expect(StudyStatus.RESCHEDULING) ?: return
        partnerCard(item.partner)
        info("Lịch hiện tại · Vẫn có hiệu lực", item.slot.display)
        info("Lịch mới chờ xác nhận", item.newSlot?.display.orEmpty())
        info("Lý do đổi lịch", item.reason)
        note("Bạn sẽ nhận thông báo khi bạn học đồng ý.")
        button("Mô phỏng bạn học đồng ý đổi lịch") { changed(study.confirm(item.id), RescheduledStudyActivity::class.java) }
        button("Về cuộc trò chuyện", Tone.PLAIN) { openConversationFor(item); finish() }
    }
}
