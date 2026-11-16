package com.ute.co_study.feature.admin.ui

import com.ute.co_study.R
import com.ute.co_study.shared.model.ReportStatus

class AdminDashboardActivity : AdminActivity(R.layout.activity_admin_dashboard) {
    override val refreshOnReturn = true

    override fun render() {
        val pending = admin.reports.queue().count { it.status == ReportStatus.ESCALATED }
        label(getString(R.string.workflow_demo))
        metrics(admin.accounts().size.toString() to "Thành viên", pending.toString() to "Chuyển cấp", admin.skills().size.toString() to "Kỹ năng")
        card("Cần Admin xem xét", "$pending báo cáo đang chờ quyết định ở cấp tài khoản.", Tone.PRIMARY)
        row("Báo cáo chuyển cấp", "Kiểm tra bằng chứng và lịch sử xử lý") { returnTo(AdminReportsActivity::class.java) }
        row("Quản lý thành viên", "Khóa, mở khóa và phân quyền") { returnTo(AdminAccountsActivity::class.java) }
        row("Nhật ký gần đây", "Theo dõi các thay đổi quản trị") { open(AdminJournalActivity::class.java) }
        row("Hồ sơ quản trị viên", "Thông tin tài khoản và quyền hạn") { open(AdminProfileActivity::class.java) }
        adminTabs(0)
    }
}
