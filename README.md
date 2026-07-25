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

## 📊 Sơ Đồ Kiến Trúc & Luồng Hệ Thống (Mermaid Diagrams)

### 1. Sơ Đồ Tổng Quan Kiến Trúc Hệ Thống (Master Combined Diagram)

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
        DTO["📦 Data Transfer Objects (DTOs)<br/>• RoomRequest (@Valid / @NotBlank)<br/>• RoomResponse"]
        ENUMS["🏷️ Enums Khách Sạn<br/>• RoomType: SINGLE | DOUBLE | SUITE | VIP<br/>• RoomStatus: AVAILABLE | OCCUPIED | MAINTENANCE"]
        SVC --- DTO & ENUMS
    end

    %% LAYER 4: PERSISTENCE & DATABASE LAYER
    subgraph Layer4["4. PERSISTENCE & DATABASE LAYER (Spring Data JPA / H2 / PostgreSQL)"]
        REPO["🗄️ RoomRepository (@Repository / JPA)<br/>• existsByRoomNumber()<br/>• findByRoomNumberContainingOrDescriptionContaining()"]
        ORM["🔥 Hibernate ORM / JDBC Driver"]
        
        subgraph SchemaDB["Cơ Sở Dữ Liệu Khách Sạn (Database Tables)"]
            T_ROOMS[("📋 Bảng ROOMS<br/>- id (PK)<br/>- room_number (UNIQUE)<br/>- room_type<br/>- price_per_night<br/>- status")]
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

### 2. Sơ Đồ Tuần Tự Luồng Tạo / Thêm Phòng Mới (Sequence Diagram)

```mermaid
sequenceDiagram
    autonumber
    actor LeTan as 👤 Lễ Tân (Frontend UI)
    participant Controller as 🎮 RoomController
    participant Service as ⚙️ RoomService
    participant Repo as 🗄️ RoomRepository
    participant DB as 💾 Database (H2/PostgreSQL)

    LeTan->>Controller: POST /api/rooms (RoomRequest JSON)
    activate Controller
    Controller->>Service: createRoom(RoomRequest)
    activate Service
    Service->>Repo: existsByRoomNumber(roomNumber)
    activate Repo
    Repo-->>Service: boolean (false)
    deactivate Repo
    Service->>Repo: save(RoomEntity)
    activate Repo
    Repo->>DB: INSERT INTO rooms VALUES(...)
    activate DB
    DB-->>Repo: Saved Room Entity
    deactivate DB
    Repo-->>Service: Saved Room Entity
    deactivate Repo
    Service-->>Controller: RoomResponse DTO
    deactivate Service
    Controller-->>LeTan: 201 Created (RoomResponse JSON)
    deactivate Controller
```

---

## 📖 Mô Tả Chi Tiết Kiến Trúc & Luồng Nghiệp Vụ (Dạng Văn Bản)

Dưới đây là phần diễn giải chi tiết toàn bộ cấu trúc hệ thống và luồng dữ liệu bằng văn bản để dễ dàng đọc, tra cứu và thuyết trình:

### 🏛️ 1. Cấu Trúc 4 Tầng Hệ Thống (4-Layer Architecture)

* **Tầng 1: Giao Diện Người Dùng (Frontend Client Layer)**
  * **Thành phần**: `src/main/resources/static/` (`index.html`, `app.js`, `style.css`).
  * **Vai trò**: Giao diện Lễ tân dạng *Glassmorphic Dark Mode*, cho phép tìm kiếm phòng, lọc theo trạng thái, điền modal chọn mẫu phòng tự động và thực hiện các thao tác thêm/sửa/xóa phòng.
  * **Tương tác**: Gửi các HTTP Request (`GET`, `POST`, `PUT`, `DELETE`) chứa payload JSON đến Backend qua Fetch API và nhận phản hồi (Response) để cập nhật giao diện không cần reload trang.

* **Tầng 2: RESTful API Controller (Spring Web Layer)**
  * **Thành phần**: `RoomController.java` (`@RestController` tại `/api/rooms`).
  * **Vai trò**: Tiếp nhận các yêu cầu HTTP từ Frontend, thực hiện xác thực và kiểm tra tính hợp lệ dữ liệu đầu vào (`@Valid` trên DTO), sau đó điều phối cuộc gọi xuống tầng Service.
  * **Kết quả**: Trả về các mã trạng thái HTTP chuẩn (`200 OK`, `201 Created`, `204 No Content`, `400 Bad Request`, `404 Not Found`).

* **Tầng 3: Xử Lý Nghiệp Vụ (Business Logic & Domain Layer)**
  * **Thành phần**: `RoomService.java` (`@Service`), các đối tượng DTO (`RoomRequest`, `RoomResponse`) và Enums (`RoomType`, `RoomStatus`).
  * **Vai trò**: Chứa toàn bộ quy tắc nghiệp vụ khách sạn (ví dụ: kiểm tra trùng lặp số phòng `existsByRoomNumber`, ánh xạ giữa DTO và Entity, bắt ngoại lệ `ResponseStatusException`).

* **Tầng 4: Lưu Trữ & Cơ Sở Dữ Liệu (Persistence & Database Layer)**
  * **Thành phần**: `RoomRepository.java` (`JpaRepository`), Hibernate ORM và CSDL (H2 / PostgreSQL).
  * **Cấu trúc Bảng CSDL**:
    1. **Bảng `ROOMS`**: Lưu thông tin phòng khách sạn (`id`, `room_number` - duy nhất, `room_type`, `price_per_night`, `status`, `description`).
    2. **Bảng `GUESTS`**: Lưu thông tin khách hàng (`id`, `full_name`, `phone`, `email`).
    3. **Bảng `BOOKINGS`**: Lưu thông tin đặt phòng (liên kết `guest_id` và `room_id`, ngày `check_in` / `check_out`).

---

### 🔄 2. Diễn Giải Luồng Xử Lý Khi Thêm Phòng Mới (Step-by-Step Flow)

1. **Khởi tạo**: Lễ tân điền thông tin phòng trên giao diện web và bấm nút **"Lưu Phòng"**.
2. **Gửi Request**: JavaScript gửi yêu cầu `POST /api/rooms` kèm dữ liệu dạng JSON.
3. **Xác thực dữ liệu**: `RoomController` nhận request và kiểm tra dữ liệu đầu vào theo Annotation trong `RoomRequest`.
4. **Kiểm tra trùng lặp**: `RoomService` gọi `RoomRepository.existsByRoomNumber()` để đảm bảo số phòng chưa tồn tại trong hệ thống.
5. **Ghi CSDL**: Nếu hợp lệ, `RoomRepository` chuyển đổi sang SQL `INSERT INTO rooms ...` để lưu vào CSDL (H2/PostgreSQL).
6. **Phản hồi**: Hệ thống trả về đối tượng `RoomResponse` kèm mã HTTP `201 Created`, Frontend nhận kết quả và tự động làm mới danh sách phòng trên màn hình.

