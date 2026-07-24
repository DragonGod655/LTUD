# Mã Nguồn Sơ Đồ Hợp Nhất Hệ Thống Quản Lý Khách Sạn (Single Master Mermaid Diagram)

Dưới đây là **Mã nguồn Sơ đồ Mermaid Hợp nhất Duy nhất (Single Master Diagram)** kết hợp toàn bộ 4 thành phần: Giao diện Client, Controller API, Tầng Nghiệp vụ DTO/Enum và Cơ sở dữ liệu ERD vào cùng một khối code.

---

## 🌐 Sơ Đồ Hợp Nhất Hệ Thống (Master Combined Diagram)

```mermaid
flowchart TD
    %% LAYER 1: FRONTEND CLIENT LAYER
    subgraph Layer1["1. FRONTEND / CLIENT LAYER (Giao Diện Lễ Tân)"]
        UI["💻 Giao Diện Lễ Tân (index.html / app.js)\n• Tìm kiếm & Lọc trạng thái phòng\n• Modal chọn phòng mẫu tự động\n• Thao tác CRUD không cần gõ chữ"]
    end

    %% LAYER 2: REST CONTROLLER LAYER
    subgraph Layer2["2. RESTFUL API CONTROLLER LAYER (Spring Web)"]
        direction TB
        CTL["🎮 RoomController (@RestController)\nBase URL: /api/rooms"]
        EP1["GET /api/rooms - Lấy danh sách phòng"]
        EP2["GET /api/rooms/{id} - Lấy chi tiết phòng"]
        EP3["POST /api/rooms - Thêm phòng mới"]
        EP4["PUT /api/rooms/{id} - Cập nhật phòng"]
        EP5["DELETE /api/rooms/{id} - Xóa phòng"]
        CTL --- EP1 & EP2 & EP3 & EP4 & EP5
    end

    %% LAYER 3: SERVICE & DOMAIN BUSINESS LAYER
    subgraph Layer3["3. BUSINESS LOGIC & DOMAIN LAYER (Spring Service & DTOs)"]
        SVC["⚙️ RoomService (@Service)\n• Kiểm tra trùng lặp số phòng\n• Ánh xạ DTO <-> Room Entity\n• Xử lý ngoại lệ ResponseStatusException"]
        DTO["📦 Data Transfer Objects (DTOs)\n• RoomRequest (@Valid / @NotBlank / @DecimalMin)\n• RoomResponse"]
        ENUMS["🏷️ Enums Khách Sạn\n• RoomType: SINGLE | DOUBLE | SUITE | VIP\n• RoomStatus: AVAILABLE | OCCUPIED | MAINTENANCE"]
        SVC --- DTO & ENUMS
    end

    %% LAYER 4: PERSISTENCE & DATABASE LAYER
    subgraph Layer4["4. PERSISTENCE & DATABASE LAYER (Spring Data JPA / H2 / PostgreSQL)"]
        REPO["🗄️ RoomRepository (@Repository / JPA)\n• existsByRoomNumber()\n• findByRoomNumberContainingOrDescriptionContaining()"]
        ORM["🔥 Hibernate ORM / JDBC Driver"]
        
        subgraph SchemaDB["Cơ Sở Dữ Liệu Khách Sạn (Database Tables)"]
            T_ROOMS[("📋 Bảng ROOMS\n- id (PK)\n- room_number (UNIQUE)\n- room_type (SINGLE/DOUBLE/SUITE/VIP)\n- price_per_night\n- status (AVAILABLE/OCCUPIED/MAINTENANCE)")]
            T_GUESTS[("👤 Bảng GUESTS\n- id (PK)\n- full_name\n- phone\n- email")]
            T_BOOKINGS[("📅 Bảng BOOKINGS\n- id (PK)\n- guest_id (FK -> GUESTS)\n- room_id (FK -> ROOMS)\n- check_in / check_out")]
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
