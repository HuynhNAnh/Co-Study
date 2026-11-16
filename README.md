# Co-Study

Co-Study là dự án Android thuộc học phần **Lập trình trên thiết bị di động**. Ứng dụng hướng đến sinh viên muốn tìm bạn học phù hợp, trao đổi kỹ năng và lên lịch học cùng nhau. Chuỗi học tập và bộ sưu tập huy hiệu giúp sinh viên theo dõi tiến độ và ghi nhận các cột mốc học chung.

Kho mã nguồn hiện có khung ứng dụng Android viết bằng Kotlin và XML. Các luồng nghiệp vụ dưới đây được mô phỏng trong prototype Figma.

## Chức năng theo vai trò

### Người dùng

- Đăng ký, đăng nhập, đặt lại mật khẩu và cập nhật hồ sơ cá nhân.
- Khai báo kỹ năng, sở thích và mục tiêu học tập để tìm bạn học phù hợp.
- Gửi đề nghị kết nối, trao đổi kỹ năng và trò chuyện với bạn học.
- Đề xuất, xác nhận, đổi hoặc hủy lịch học. Sau buổi học, người dùng có thể xác nhận hoàn thành và đánh giá bạn học.
- Xem thông báo và báo cáo hành vi vi phạm.

#### Chuỗi học tập (streak)

Chuỗi học tập ghi nhận số ngày liên tiếp có buổi học hợp lệ. Người dùng có thể xem chuỗi hiện tại, lịch sử các ngày đã ghi nhận và trạng thái xác nhận của từng buổi. Một ngày bị bỏ lỡ sẽ ngắt chuỗi. Buổi học hợp lệ tiếp theo bắt đầu chuỗi mới.

Theo quy tắc minh họa trong prototype, buổi học cần kéo dài ít nhất **25 phút** và được cả hai bạn học xác nhận trong vòng **48 giờ**. Mỗi ngày theo giờ Việt Nam được tính tối đa một lần vào chuỗi.

#### Bộ sưu tập huy hiệu

Người dùng có thể xem huy hiệu đã nhận, điều kiện mở khóa và chọn huy hiệu hiển thị trên hồ sơ cá nhân. Prototype minh họa hai cách mở khóa huy hiệu.

| Loại huy hiệu | Điều kiện |
| --- | --- |
| Chuỗi học tập | Duy trì chuỗi **3, 7 hoặc 30 ngày liên tiếp** |
| Học cùng bạn | Hoàn thành **5 buổi học hợp lệ** với cùng một bạn học |

### Kiểm duyệt viên

- Tiếp nhận báo cáo, xem bằng chứng và yêu cầu bổ sung thông tin khi cần.
- Xử lý báo cáo vi phạm hoặc chuyển vụ việc cần xem xét thêm cho quản trị viên.
- Kiểm tra phản ánh về kết quả ghi nhận buổi học và theo dõi lịch sử xử lý.

### Quản trị viên (Admin)

- Quản lý tài khoản, khóa hoặc mở khóa tài khoản và phân quyền thành viên.
- Xem xét báo cáo do kiểm duyệt viên chuyển lên.
- Quản lý danh mục kỹ năng, cấu hình hệ thống và nhật ký quản trị.
- Quản lý quy tắc ghi nhận chuỗi học tập và điều kiện trao huy hiệu.

## Chạy dự án Android

Cài Android Studio tương thích với Android Gradle Plugin **9.3.3** và cài **Android SDK 37** trong SDK Manager. Cấu hình dự án yêu cầu **Java 25** cho Gradle Daemon. Thiết bị chạy ứng dụng cần **Android 7.0 (API 24)** trở lên.

1. Tải mã nguồn về máy.

   ```bash
   git clone https://github.com/HuynhNAnh/Co-Study.git
   ```

2. Mở thư mục `Co-Study` trong Android Studio và chờ Gradle Sync hoàn tất.
3. Chọn cấu hình `app`, chọn thiết bị Android hoặc trình giả lập, rồi nhấn **Run**.

## Cấu trúc kho mã nguồn

### Cấu trúc hiện tại

```text
Co-Study/
├── README.md
├── .gitignore
├── .idea/                         # Cấu hình Android Studio của dự án
├── build.gradle.kts               # Cấu hình Gradle cấp dự án
├── settings.gradle.kts            # Khai báo module :app
├── gradle.properties
├── gradlew
├── gradlew.bat
├── gradle/
│   ├── libs.versions.toml          # Phiên bản thư viện và plugin
│   ├── gradle-daemon-jvm.properties
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── figma/                         # Ảnh xuất từ prototype
│   ├── 01_Nguoi_dung/
│   ├── 02_Kiem_duyet_vien/
│   └── 03_Admin/
└── app/
    ├── .gitignore
    ├── build.gradle.kts
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── java/com/ute/co_study/
        │   │   ├── MainActivity.kt
        │   │   └── feature/        # Package MVVM theo vai trò
        │   ├── res/
        │   │   ├── layout/         # Giao diện XML
        │   │   ├── drawable/       # Hình và nền dùng trong ứng dụng
        │   │   ├── mipmap-*/       # Biểu tượng launcher theo mật độ màn hình
        │   │   ├── values/         # Chuỗi, màu sắc và giao diện
        │   │   ├── values-night/   # Giao diện tối
        │   │   └── xml/            # Quy tắc sao lưu và trích xuất dữ liệu
        │   └── keepRules/          # Quy tắc giữ mã khi tối ưu hóa
        ├── test/java/com/ute/co_study/         # Kiểm thử JVM
        └── androidTest/java/com/ute/co_study/  # Kiểm thử trên thiết bị
```

Thư mục `figma/` dùng để tra cứu thiết kế. Hình dùng trực tiếp trong ứng dụng đặt dưới `app/src/main/res/`.

| Thư mục ảnh | Nội dung |
| --- | --- |
| [01_Nguoi_dung](figma/01_Nguoi_dung/) | 36 ảnh giao diện người dùng, trò chuyện và lịch học |
| [02_Kiem_duyet_vien](figma/02_Kiem_duyet_vien/) | 21 ảnh giao diện kiểm duyệt và quy trình xử lý báo cáo |
| [03_Admin](figma/03_Admin/) | 25 ảnh giao diện quản trị hệ thống |

### Cấu trúc MVVM theo vai trò

Ứng dụng dùng một module Gradle `:app`. Ba vai trò có cùng cấu trúc package dưới `app/src/main/java/com/ute/co_study/`. Các thư mục chứa `.gitkeep` là vị trí dành cho mã sẽ được thêm khi triển khai từng chức năng.

~~~text
com.ute.co_study/
├── MainActivity.kt
└── feature/
    ├── learner/                   # Người dùng
    │   ├── ui/
    │   ├── viewmodel/
    │   ├── model/
    │   └── data/repository/
    ├── moderation/                # Kiểm duyệt viên
    │   ├── ui/
    │   ├── viewmodel/
    │   ├── model/
    │   └── data/repository/
    └── admin/                     # Quản trị viên
        ├── ui/
        ├── viewmodel/
        ├── model/
        └── data/repository/
~~~

| Package | Trách nhiệm |
| --- | --- |
| `ui/` | Fragment, adapter và mã View Binding. UI gửi thao tác tới ViewModel và hiển thị trạng thái nhận được. |
| `viewmodel/` | ViewModel và lớp `UiState` của từng màn hình. ViewModel gọi repository và công bố trạng thái để UI quan sát theo vòng đời. |
| `model/` | Mô hình nghiệp vụ riêng của vai trò, dùng Kotlin thuần. |
| `data/repository/` | Repository cung cấp dữ liệu và tập trung các thao tác ghi. Khi có nguồn dữ liệu, đặt mã truy cập dưới `data/local/` hoặc `data/remote/`. |

Khi triển khai một chức năng, dùng cùng tên package con ở `ui/` và `viewmodel/`. Ví dụ hồ sơ người dùng đặt ở `learner/ui/profile/` và `learner/viewmodel/profile/`. Dùng `moderation/ui/report/` cho xử lý báo cáo và `admin/ui/account/` cho quản lý tài khoản.

Khi triển khai đăng nhập hoặc dữ liệu dùng chung, đặt luồng xác thực dưới `feature/auth/` và model, repository dùng chung dưới `shared/`. Ví dụ, ba vai trò có thể dùng chung một repository cho dữ liệu báo cáo và mỗi vai trò có ViewModel riêng cho màn hình của mình.

Tham khảo [hướng dẫn kiến trúc ứng dụng Android](https://developer.android.com/topic/architecture) về trách nhiệm của UI, ViewModel và repository.

## Quy ước đặt tên tệp

Dùng từ tiếng Anh ngắn, mô tả chức năng hoặc nội dung của tệp. Dùng cùng một từ cho cùng một chức năng, ví dụ `Profile` trong Kotlin và `profile` trong XML. Tránh tên chung chung như `Utils.kt`, `screen_1.xml` và tên gắn với người dùng mẫu. Giữ các tên chuẩn của dự án và công cụ, ví dụ `README.md`, `AndroidManifest.xml`, `build.gradle.kts` và `.gitignore`.

| Loại tệp | Cách đặt tên | Ví dụ |
| --- | --- | --- |
| Kotlin (`.kt`) | `PascalCase`. Nếu tệp có một lớp hoặc interface chính, lấy tên đó làm tên tệp. Thêm hậu tố thể hiện trách nhiệm khi cần. | `ProfileFragment.kt`, `ProfileViewModel.kt`, `ProfileUiState.kt`, `ReportRepository.kt`, `StudySession.kt` |
| Kiểm thử Kotlin (`.kt`) | Thêm `Test` vào tên lớp được kiểm thử. Dùng cùng package với mã được kiểm thử và đặt tệp trong `src/test/` hoặc `src/androidTest/`. | `ProfileViewModelTest.kt`, `ProfileFragmentTest.kt` |
| Layout Android (`res/layout/`) | `snake_case` theo mẫu `<type>_<name>.xml`. Dùng tiền tố `activity_`, `fragment_`, `dialog_` hoặc `item_`. Thêm tên vai trò sau tiền tố nếu cần phân biệt các màn hình cùng tên. | `activity_main.xml`, `fragment_learner_profile.xml`, `fragment_admin_profile.xml`, `item_study_session.xml` |
| Tài nguyên Android khác (`res/`) | `snake_case`. Dùng `ic_` cho biểu tượng, `bg_` cho nền và `img_` cho ảnh minh họa. | `ic_calendar.xml`, `bg_profile_card.xml`, `img_study_group.webp` |
| Tài liệu và ảnh xuất từ Figma | Dùng chữ thường và dấu gạch nối. Với ảnh Figma, giữ mã màn hình ở đầu nếu mã đó được dùng để đối chiếu luồng. | `setup-guide.md`, `u01-login.png`, `chat-03-propose-session.png`, `m02-reports.png` |

Áp dụng quy ước khi thêm hoặc đổi tên tệp.

## Thành viên dự án

| Thành viên | Công việc |
| --- | --- |
| Huỳnh Ngọc Anh | Phối hợp quản lý Trello, xây dựng chức năng hồ sơ, ghép cặp và trao đổi kỹ năng |
| Đặng Thái Nguyên | Xây dựng và hoàn thiện nội dung dự án theo phân công nhóm |

## Liên kết dự án

- [Mã nguồn Co-Study trên GitHub](https://github.com/HuynhNAnh/Co-Study)
- [Thiết kế và prototype Co-Study trên Figma](https://www.figma.com/design/iTZkd0nWiIRL7BymVElCja/Co-Study?m=auto&t=DIT3j7JqIclm1wUl-6)
- [Bảng phân công công việc trên Trello](https://trello.com/b/pCVt8mXD/126ltdd01co-study-%E1%BB%A9ng-d%E1%BB%A5ng-k%E1%BA%BFt-n%E1%BB%91i-va-ghep-c%E1%BA%B7p-h%E1%BB%8Dc-t%E1%BA%ADp-cho-sinh-vien)
- [Tài liệu tham khảo của dự án](https://docs.google.com/document/d/1Ab1jf46Tx87avLxAyYE_pUHTrtohG1Lg/edit?usp=sharing&ouid=105692628120061086882&rtpof=true&sd=true)
