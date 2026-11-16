package com.ute.co_study.feature.learner.ui.progress

import com.ute.co_study.R
import com.ute.co_study.feature.learner.ui.study.StudyActivity

class StreakActivity : StudyActivity(R.layout.activity_streak) {
    override val refreshOnReturn = true

    override fun render() {
        val progress = study.progress()
        val policy = study.policy()
        metrics(progress.current.toString() to "Ngày hiện tại", progress.longest.toString() to "Kỷ lục", progress.validDays.toString() to "Ngày ghi nhận")
        if (progress.current == 0) note("Hoàn thành một buổi học hợp lệ để bắt đầu chuỗi.")
        card("Điều kiện ghi nhận", "Buổi học từ ${policy.minimumMinutes} phút. Cả hai xác nhận trong ${policy.confirmationHours} giờ. Mỗi ngày theo giờ Việt Nam chỉ được tính một lần.")
        row("Lịch sử ghi nhận", "Theo dõi trạng thái của từng buổi học") { open(StreakHistoryActivity::class.java) }
        row("Bộ sưu tập huy hiệu", "Xem điều kiện và chọn huy hiệu cho hồ sơ") { open(BadgesActivity::class.java) }
    }
}
