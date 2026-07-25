# Kịch Bản Thuyết Trình Đồ Án - Hệ Thống Quản Lý Khách Sạn (Hotel Management System)

Tài liệu hướng dẫn kịch bản nói, cách trình chiếu sơ đồ và bộ câu hỏi phản biện dành cho buổi thuyết trình.

---

## 📌 Phần 1: Lời Mở Đầu & Giới Thiệu Tổng Quan (1 - 2 phút)

**Lời nói mẫu:**
> "Kính chào Thầy/Cô và các bạn. Em xin đại diện nhóm trình bày đồ án **Hệ Thống Quản Lý Khách Sạn (Hotel Management System)**.
> 
> Mục tiêu của đồ án là xây dựng một hệ thống quản lý phòng khách sạn toàn diện theo kiến trúc chuẩn Spring Boot, cung cấp các chuẩn RESTful API mạnh mẽ, quản lý cơ sở dữ liệu quan hệ và tích hợp giao diện Lễ tân điều hành trực quan.
> 
> **Các công nghệ trọng tâm được áp dụng:**
> - **Backend**: Java 25 LTS, Spring Boot 3.5.3, Spring Data JPA, Bean Validation.
> - **Database**: H2 In-Memory DB (phục vụ demo nhanh) & PostgreSQL 16 (triển khai Docker).
> - **Frontend**: HTML5, CSS3 Glassmorphic Dark UI, JavaScript ES6 Async/Await."

---

## 📊 Phần 2: Trình Bày Kiến Trúc & Sơ Đồ Mermaid Hợp Nhất (2 - 3 phút)

*(Mở trang [README.md](file:///c:/Users/Admin/Documents/GitHub/LTUD/README.md) hoặc [Mermaid Live Editor](https://mermaid.live) hiển thị Sơ đồ Hợp nhất Master)*

**Lời nói mẫu:**
> "Hệ thống của chúng em được thiết kế chia làm 4 tầng (4-Layer Architecture) phân tách trách nhiệm rõ ràng:
> 
> 1. **Tầng Client (Frontend UI)**: Giao diện Hotel Desk hiện đại, cho phép lễ tân tìm kiếm phòng realtime, lọc theo trạng thái và chọn các mẫu phòng có sẵn mà không cần gõ chữ thủ công.
> 2. **Tầng API Controller (`RoomController`)**: Tiếp nhận các truy vấn HTTP RESTful (`GET`, `POST`, `PUT`, `DELETE /api/rooms`) và kiểm tra tính hợp lệ của dữ liệu (Validation).
> 3. **Tầng Nghiệp Vụ (`RoomService` & DTOs)**: Xử lý logic nghiệp vụ như kiểm tra trùng số phòng, ánh xạ dữ liệu DTO (`RoomRequest`, `RoomResponse`) và quản lý các trạng thái `AVAILABLE`, `OCCUPIED`, `MAINTENANCE`.
> 4. **Tầng Lưu Trữ & CSDL (`RoomRepository` & Database)**: Sử dụng Spring Data JPA và Hibernate ORM để lưu trữ xuống các bảng `ROOMS`, `GUESTS`, `BOOKINGS`."

---

## 💻 Phần 3: Demo Thực Tế Ứng Dụng (3 phút)

### Các bước demo thao tác trực tiếp:
1. **Mở trang chủ**: Truy cập `http://localhost:8080` giới thiệu tổng quan giao diện Lễ tân.
2. **Thao tác Thêm phòng mới**: 
   - Bấm nút **"+ Thêm phòng mới"**.
   - Chọn phòng mẫu (ví dụ: *Penthouse VIP P501* hoặc *Phòng đôi Deluxe P206*).
   - Chỉ ra việc tên, mã phòng, giá tiền/đêm và mô tả được tự động nạp.
   - Nhấp **"Lưu thông tin phòng"** $\rightarrow$ Chỉ ra phòng mới xuất hiện ngay trên danh sách.
3. **Tìm kiếm & Lọc trạng thái**:
   - Gõ từ khóa tìm kiếm (ví dụ: `VIP` hoặc `P101`).
   - Lọc theo từng trạng thái: `🟢 Phòng trống`, `🟡 Đang có khách`, `🔴 Bảo trì`.
4. **Minh chứng REST API & CSDL**:
   - Mở tab mới truy cập `http://localhost:8080/api/rooms` để minh chứng dữ liệu trả về dạng JSON chuẩn.
   - Mở `http://localhost:8080/h2-console` để xem bảng dữ liệu trong CSDL H2.

---

## 🎯 Phần 4: Bộ Câu Hỏi Phản Biện Thường Gặp (Q&A)

### ❓ Câu 1: Làm thế nào để hệ thống đảm bảo số phòng không bị trùng lặp?
- **Trả lời**: Hệ thống áp dụng 2 lớp bảo vệ:
  - Tầng CSDL: Cột `room_number` có ràng buộc `UNIQUE`.
  - Tầng Backend: Trước khi lưu, `RoomService` sẽ gọi `roomRepository.existsByRoomNumber()` để kiểm tra, nếu trùng sẽ trả về lỗi `400 Bad Request`.

### ❓ Câu 2: Sự khác biệt giữa môi trường Dev (H2) và Production (PostgreSQL) là gì?
- **Trả lời**: Hệ thống sử dụng cơ chế Spring Profiles trong [application.yml](file:///c:/Users/Admin/Documents/GitHub/LTUD/src/main/resources/application.yml). Mặc định dùng H2 In-Memory DB giúp chạy nhanh không cần cài CSDL. Khi triển khai thật, chỉ cần bật profile `postgres` để kết nối đến PostgreSQL 16 qua Docker.

### ❓ Câu 3: Frontend giao tiếp với Backend bằng phương thức gì?
- **Trả lời**: Frontend gửi các truy vấn bất đồng bộ `fetch()` mang định dạng JSON đến các Endpoint của `RoomController`. Dữ liệu trả về dưới dạng `ResponseEntity<RoomResponse>` được JavaScript render lại lên màn hình mà không cần tải lại toàn bộ trang (No-reload).
