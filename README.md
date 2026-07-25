# Hệ Thống Quản Lý Khách Sạn (Hotel Management System)

Ứng dụng quản lý phòng khách sạn hoàn chỉnh được phát triển theo kiến trúc Spring Boot, tích hợp Backend RESTful API, Cơ sở dữ liệu JPA (H2 / PostgreSQL) và Giao diện người dùng Lễ tân (Hotel Desk Dark Mode UI).

---

## 🚀 Công Nghệ Sử Dụng

- **Ngôn ngữ & Framework**: Java 25 LTS, Spring Boot 3.5.3 (Spring Web, Spring Data JPA, Bean Validation).
- **Cơ sở dữ liệu**:
  - *Dev / Demo*: H2 In-Memory Database (Tự động nạp dữ liệu mẫu ban đầu).
  - *Môi trường thật*: PostgreSQL 16 (Tích hợp qua Docker Compose).
- **Giao diện người dùng (Frontend)**: HTML5, CSS3 (Glassmorphic Dark Theme), JavaScript ES6 (Fetch API).
- **Đóng gói & Công cụ**: Apache Maven 3.9+, Docker Compose.

---

## 🛠️ Hướng Dẫn Chạy Ứng Dụng

### Yêu cầu môi trường:
- JDK 25+
- Maven 3.9+

### Lệnh chạy nhanh:
```powershell
mvn spring-boot:run
```

### Đường dẫn truy cập:
- **Giao diện Web Quản lý Khách sạn**: [http://localhost:8080](http://localhost:8080)
- **H2 Database Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - **JDBC URL**: `jdbc:h2:mem:courses`
  - **User**: `sa`
  - **Password**: *(bỏ trống)*

---

## 📡 Danh Sách RESTful API

| Phương thức | Endpoint | Mô tả |
| :--- | :--- | :--- |
| `GET` | `/api/rooms` | Lấy danh sách tất cả các phòng (Có hỗ trợ tìm kiếm `?search=...`) |
| `GET` | `/api/rooms/{id}` | Lấy thông tin chi tiết một phòng theo ID |
| `POST` | `/api/rooms` | Thêm mới thông tin phòng khách sạn |
| `PUT` | `/api/rooms/{id}` | Cập nhật thông tin phòng theo ID |
| `DELETE` | `/api/rooms/{id}` | Xóa thông tin phòng khỏi hệ thống |

---

## 📊 Mô Tả Kiến Trúc & Luồng Hệ Thống (GitHub Visual Diagram)

### 1. Sơ Đồ Tổng Quan Kiến Trúc 4 Tầng (System Architecture Diagram)

GitHub sẽ tự động hiển thị sơ đồ trực quan dưới đây từ mã nguồn Mermaid:

```mermaid
flowchart TD
    %% LAYER 1: FRONTEND CLIENT LAYER
    subgraph Layer1["1. FRONTEND / CLIENT LAYER (Giao Diện Lễ Tân)"]
        UI["💻 Giao Diện Lễ Tân (index.html / app.js)<br/>• Tìm kiếm & Lọc trạng thái phòng<br/>• Modal chọn phòng mẫu tự động<br/>• Thao tác CRUD không cần gõ chữ"]
    end

    %% LAYER 2: REST CONTROLLER LAYER
    subgraph Layer2["2. RESTFUL API CONTROLLER LAYER (Spring Web)"]
        direction TB
        CTL["🎮 RoomController (@RestController)<br/>Base URL: /api/rooms"]
        EP1["GET /api/rooms - Lấy danh sách phòng"]
        EP2["GET /api/rooms/{id} - Lấy chi tiết phòng"]
        EP3["POST /api/rooms - Thêm phòng mới"]
        EP4["PUT /api/rooms/{id} - Cập nhật phòng"]
        EP5["DELETE /api/rooms/{id} - Xóa phòng"]
        CTL --- EP1 & EP2 & EP3 & EP4 & EP5
    end

    %% LAYER 3: SERVICE & DOMAIN BUSINESS LAYER
    subgraph Layer3["3. BUSINESS LOGIC & DOMAIN LAYER (Spring Service & DTOs)"]
        SVC["⚙️ RoomService (@Service)<br/>• Kiểm tra trùng lặp số phòng<br/>• Ánh xạ DTO <-> Room Entity<br/>• Xử lý ngoại lệ ResponseStatusException"]
        DTO["📦 Data Transfer Objects (DTOs)<br/>• RoomRequest (@Valid / @NotBlank / @DecimalMin)<br/>• RoomResponse"]
        ENUMS["🏷️ Enums Khách Sạn<br/>• RoomType: SINGLE | DOUBLE | SUITE | VIP<br/>• RoomStatus: AVAILABLE | OCCUPIED | MAINTENANCE"]
        SVC --- DTO & ENUMS
    end

    %% LAYER 4: PERSISTENCE & DATABASE LAYER
    subgraph Layer4["4. PERSISTENCE & DATABASE LAYER (Spring Data JPA / H2 / PostgreSQL)"]
        REPO["🗄️ RoomRepository (@Repository / JPA)<br/>• existsByRoomNumber()<br/>• findByRoomNumberContainingOrDescriptionContaining()"]
        ORM["🔥 Hibernate ORM / JDBC Driver"]
        
        subgraph SchemaDB["Cơ Sở Dữ Liệu Khách Sạn (Database Tables)"]
            T_ROOMS[("📋 Bảng ROOMS<br/>- id (PK)<br/>- room_number (UNIQUE)<br/>- room_type (SINGLE/DOUBLE/SUITE/VIP)<br/>- price_per_night<br/>- status (AVAILABLE/OCCUPIED/MAINTENANCE)")]
            T_GUESTS[("👤 Bảng GUESTS<br/>- id (PK)<br/>- full_name<br/>- phone<br/>- email")]
            T_BOOKINGS[("📅 Bảng BOOKINGS<br/>- id (PK)<br/>- guest_id (FK -> GUESTS)<br/>- room_id (FK -> ROOMS)<br/>- check_in / check_out")]
            T_GUESTS -- "1..N (Đăng ký đặt phòng)" --> T_BOOKINGS
            T_ROOMS -- "1..N (Được đặt)" --> T_BOOKINGS
        end
        
        REPO --> ORM
        ORM --> SchemaDB
    end

    %% INTER-LAYER CONNECTIVITY
    UI -- "HTTP Request (JSON Payload)" --> CTL
    CTL -- "Trả kết quả JSON (200 OK / 201 Created)" --> UI
    CTL -- "Truyền DTO Dữ Liệu" --> SVC
    SVC -- "Thực Thi Nghiệp Vụ JPA" --> REPO
```

---

### 🏛️ 2. Chi Tiết Các Tầng Hệ Thống (Text Breakdown)

| Tầng | Thành Phần Chính | Chức Năng & Nhiệm Vụ |
| :--- | :--- | :--- |
| **1. Client Layer** | `index.html`, `app.js`, `style.css` | Hiển thị giao diện lễ tân Dark Mode, gửi AJAX request đến Backend và làm mới UI. |
| **2. Controller Layer** | `RoomController.java` | Tiếp nhận REST API request (`/api/rooms`), validate dữ liệu đầu vào và trả về HTTP Status. |
| **3. Business Layer** | `RoomService.java`, DTOs, Enums | Thực thi logic nghiệp vụ, kiểm tra trùng lặp số phòng, chuyển đổi DTO và Entity. |
| **4. Database Layer** | `RoomRepository.java`, H2/PostgreSQL | Lưu trữ dữ liệu lâu dài vào các bảng `ROOMS`, `GUESTS`, `BOOKINGS` qua Hibernate JPA. |

---

### 🔄 3. Sơ Đồ Tuần Tự Luồng Xử Lý Khi Thêm Phòng Mới (Sequence Diagram)

```mermaid
sequenceDiagram
    autonumber
    actor LT as 💻 Lễ Tân UI
    participant CTL as 🎮 RoomController
    participant SVC as ⚙️ RoomService
    participant REPO as 🗄️ RoomRepository
    participant DB as 💾 Database (H2/Postgres)

    LT->>CTL: POST /api/rooms (JSON Payload)
    CTL->>CTL: Validate dữ liệu (@Valid RoomRequest)
    CTL->>SVC: createRoom(RoomRequest)
    SVC->>REPO: existsByRoomNumber(roomNumber)
    alt Số phòng đã tồn tại
        REPO-->>SVC: true
        SVC-->>CTL: Ném ngoại lệ ResponseStatusException (400 Bad Request)
        CTL-->>LT: Trả về HTTP 400 Bad Request (Thông báo trùng phòng)
    else Số phòng chưa tồn tại
        REPO-->>SVC: false
        SVC->>REPO: save(Room Entity)
        REPO->>DB: INSERT INTO rooms VALUES(...)
        DB-->>REPO: Trả về Saved Entity
        REPO-->>SVC: Trả về Room Entity
        SVC-->>CTL: Trả về RoomResponse DTO
        CTL-->>LT: Trả về HTTP 201 Created (JSON Response)
    end
```

#### Các Bước Thực Hiện Chi Tiết:
1. **Khởi tạo**: Lễ tân điền thông tin phòng trên giao diện web và bấm nút **"Lưu Phòng"**.
2. **Gửi Request**: JavaScript gửi yêu cầu `POST /api/rooms` kèm dữ liệu dạng JSON.
3. **Xác thực dữ liệu**: `RoomController` nhận request và kiểm tra dữ liệu đầu vào theo Annotation trong `RoomRequest`.
4. **Kiểm tra trùng lặp**: `RoomService` gọi `RoomRepository.existsByRoomNumber()` để đảm bảo số phòng chưa tồn tại trong hệ thống.
5. **Ghi CSDL**: Nếu hợp lệ, `RoomRepository` chuyển đổi sang SQL `INSERT INTO rooms ...` để lưu vào CSDL (H2/PostgreSQL).
6. **Phản hồi**: Hệ thống trả về đối tượng `RoomResponse` kèm mã HTTP `201 Created`, Frontend nhận kết quả và tự động làm mới danh sách phòng trên màn hình.


