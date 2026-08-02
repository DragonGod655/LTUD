-- Hotel Management Database Schema (MySQL 8.0+)
CREATE DATABASE IF NOT EXISTS hoteldb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hoteldb;

-- 1. Table Rooms
CREATE TABLE IF NOT EXISTS rooms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(30) NOT NULL UNIQUE,
    room_type VARCHAR(30) NOT NULL,
    description VARCHAR(1000),
    price_per_night DECIMAL(12,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_rooms_status (status),
    INDEX idx_rooms_type (room_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Table Guests
CREATE TABLE IF NOT EXISTS guests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100),
    identity_card VARCHAR(30),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Table Bookings
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    guest_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    check_in DATETIME NOT NULL,
    check_out DATETIME NOT NULL,
    total_price DECIMAL(12,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    note VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_bookings_guest FOREIGN KEY (guest_id) REFERENCES guests(id) ON DELETE CASCADE,
    CONSTRAINT fk_bookings_room FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
    INDEX idx_bookings_dates (check_in, check_out)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Seed Sample Data for MySQL
INSERT INTO rooms (room_number, room_type, description, price_per_night, status) VALUES
('P101', 'SINGLE', 'Phòng đơn tiêu chuẩn, giường 1.4m, hướng thành phố.', 500000.00, 'AVAILABLE'),
('P202', 'DOUBLE', 'Phòng đôi cao cấp, 2 giường đơn hoặc 1 giường đôi lớn, ban công thoáng mát.', 850000.00, 'AVAILABLE'),
('P303', 'SUITE', 'Phòng Suite gia đình sang trọng, có phòng khách riêng và bồn tắm panorama.', 1800000.00, 'OCCUPIED'),
('P501', 'VIP', 'Penthouse VIP thượng hạng, tầm nhìn toàn cảnh biển, phục vụ butler 24/7.', 4500000.00, 'AVAILABLE'),
('P104', 'SINGLE', 'Phòng đơn đang bảo trì hệ thống điều hòa.', 450000.00, 'MAINTENANCE')
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

INSERT INTO guests (full_name, phone, email, identity_card) VALUES
('Nguyễn Văn An', '0912345678', 'an.nguyen@example.com', '012345678901'),
('Trần Thị Bình', '0987654321', 'binh.tran@example.com', '098765432109')
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

INSERT INTO bookings (guest_id, room_id, check_in, check_out, total_price, status, note) VALUES
(1, 3, '2026-08-01 14:00:00', '2026-08-05 12:00:00', 7200000.00, 'CHECKED_IN', 'Khách nhận phòng sớm')
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;
