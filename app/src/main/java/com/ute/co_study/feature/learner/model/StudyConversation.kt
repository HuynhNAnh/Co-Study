package com.ute.co_study.feature.learner.model

internal enum class StudyConversation(
    val title: String,
    val initials: String,
    val subtitle: String,
    val studyTopic: String?,
    val initialMessages: List<StudyMessage>
) {
    MINH_ANH("Minh Anh", "MA", "Bạn học · Kotlin / Android", "Kotlin Activity & Intent", dialogue(
        "Chào bạn! Mình có thể hỗ trợ phần Activity và Intent trong Kotlin.",
        "Tuyệt! Mình muốn thực hành truyền dữ liệu giữa các màn hình.",
        "Bạn thử mở màn chi tiết bằng Intent và gửi mã bài tập qua putExtra nhé.",
        "Mình sẽ làm thử rồi ghi lại đoạn chưa hiểu.",
        "Mình cùng ôn Kotlin nhé? Bạn chọn lịch phù hợp nha."
    )),
    ANDROID("Nhóm Android K26", "A", "Nhóm học Android", null, dialogue(
        "Hoàng: Mình vừa chia sẻ tài liệu về vòng đời Activity.",
        "Mọi người muốn chia nhau chuẩn bị phần nào?",
        "Huy: Mình làm ví dụ ViewModel và xoay màn hình.",
        "Mình sẽ chuẩn bị ví dụ truyền dữ liệu giữa hai Activity.",
        "Minh Anh: Tối nay tụi mình góp ví dụ vào một danh sách nhé."
    )),
    HOANG_NAM("Hoàng Nam", "HN", "Bạn học · SQL / Cơ sở dữ liệu", "SQL JOIN và GROUP BY", dialogue(
        "Mình có bộ dữ liệu sinh viên và môn học để luyện SQL.",
        "Có thể bắt đầu từ LEFT JOIN được không?",
        "Được, mình sẽ thêm vài sinh viên chưa đăng ký môn để dễ thấy kết quả.",
        "Mình thử viết thêm truy vấn đếm số môn theo từng sinh viên nhé.",
        "Cảm ơn bạn, hẹn buổi học sau!"
    )),
    DUC_HUY("Đức Huy", "ĐH", "Bạn học · Jetpack Compose", "Jetpack Compose · State và danh sách", dialogue(
        "Bạn từng chuyển một màn XML sang Compose chưa?",
        "Mình mới thử với một thẻ thông tin đơn giản.",
        "Buổi tới mình có thể dùng LazyColumn và lọc danh sách bằng state.",
        "Để mình chuẩn bị danh sách kỹ năng làm ví dụ.",
        "Tuyệt. Mình sẽ mang theo ví dụ về rememberSaveable."
    )),
    BAO_NGOC("Bảo Ngọc", "BN", "Bạn học · Figma / UI", "Figma Auto Layout", dialogue(
        "Mình đã phác bố cục cho màn ghép cặp trong Figma.",
        "Mình muốn hiểu cách giữ khoảng cách khi chữ xuống dòng.",
        "Ta dùng Auto Layout và thử đổi nội dung dài ngắn trên cùng một thẻ.",
        "Mình gửi bạn hai mẫu thẻ để so sánh nhé.",
        "Được, mình rảnh tối thứ Bảy để xem cùng bạn."
    )),
    PHUONG_LINH("Phương Linh", "PL", "Bạn học · Tiếng Anh giao tiếp", "Luyện giới thiệu dự án bằng tiếng Anh", dialogue(
        "Mình đang chuẩn bị phần giới thiệu đồ án bằng tiếng Anh.",
        "Bạn muốn tập phần mở đầu hay trả lời câu hỏi?",
        "Mở đầu trước nha. Mỗi người trình bày trong một phút.",
        "Mình sẽ viết vài câu về vai trò của mình trong nhóm.",
        "Tốt rồi. Mình sẽ chuẩn bị câu hỏi tiếp nối."
    )),
    THU_HA("Thu Hà", "TH", "Bạn học · Python / Phân tích dữ liệu", "Python · Làm sạch dữ liệu CSV", dialogue(
        "Mình có một bộ dữ liệu khảo sát nhỏ, bạn muốn thử làm sạch không?",
        "Có, mình đang tìm ví dụ về giá trị thiếu.",
        "Ta thử đọc CSV và so sánh kết quả trước, sau khi xử lý.",
        "Mình sẽ chuẩn bị vài biểu đồ đơn giản để kiểm tra.",
        "Nhớ đem theo một bảng dữ liệu có dòng trùng để cùng thử nhé."
    )),
    HAI_YEN("Hải Yến", "HY", "Bạn học · Git / Kotlin", "Git · Giải quyết xung đột khi gộp nhánh", dialogue(
        "Mình có thể tạo một repo nhỏ để tập xử lý xung đột Git.",
        "Hay đó, mình từng bị kẹt khi cả hai người sửa cùng một tệp.",
        "Mỗi người làm một nhánh rồi mình xem diff trước khi gộp.",
        "Mình sẽ ghi lại các lệnh để lần sau làm được một mình.",
        "Lịch cũ mình bận mất rồi, mình hẹn bạn buổi khác nhé."
    )),
    QUANG_MINH("Quang Minh", "QM", "Bạn học · C++ / Thuật toán", "Quy hoạch động · Dãy con", dialogue(
        "Mình đang luyện các bài dãy con bằng quy hoạch động.",
        "Mình hay nhầm thế nào là dãy con liên tiếp.",
        "Lấy một ví dụ ngắn rồi viết các trạng thái ra giấy trước nhé.",
        "Mình sẽ tự thử với dãy có số âm.",
        "Ổn đó. Buổi tới ta đối chiếu hai cách giải."
    )),
    STUDY_GROUP("Nhóm Ôn thi lập trình", "OT", "Nhóm học thuật toán", null, dialogue(
        "Minh: Cuối tuần nhóm mình ôn quy hoạch động nha.",
        "Mình có thể chuẩn bị bài mở đầu về dãy con.",
        "Hà: Mình sẽ thêm một bài đường đi trên lưới.",
        "Mọi người viết ý tưởng trước rồi hãy xem lời giải nhé.",
        "Yến: Mình sẽ tổng hợp những điểm dễ nhầm sau buổi học."
    ));

    val isGroup: Boolean get() = studyTopic == null

    companion object {
        fun forPartner(name: String): StudyConversation? = entries.firstOrNull { it.title == name }
    }
}

internal data class StudyMessage(val text: String, val sentByLearner: Boolean)

private fun dialogue(vararg lines: String): List<StudyMessage> =
    lines.mapIndexed { index, text -> StudyMessage(text, sentByLearner = index % 2 != 0) }
