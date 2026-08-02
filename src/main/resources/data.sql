-- Seed data for Rooms
INSERT INTO rooms (room_number, room_type, description, price_per_night, status, created_at, updated_at) VALUES
('P101', 'SINGLE', 'Phòng đơn tiêu chuẩn, giường 1.4m, hướng thành phố.', 500000, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P202', 'DOUBLE', 'Phòng đôi cao cấp, 2 giường đơn hoặc 1 giường đôi lớn, ban công thoáng mát.', 850000, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P303', 'SUITE', 'Phòng Suite gia đình sang trọng, có phòng khách riêng và bồn tắm panorama.', 1800000, 'OCCUPIED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P501', 'VIP', 'Penthouse VIP thượng hạng, tầm nhìn toàn cảnh biển, phục vụ butler 24/7.', 4500000, 'AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P104', 'SINGLE', 'Phòng đơn đang bảo trì hệ thống điều hòa.', 450000, 'MAINTENANCE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Seed data for Guests
INSERT INTO guests (full_name, phone, email, identity_card, created_at, updated_at) VALUES
('Nguyễn Văn An', '0912345678', 'an.nguyen@example.com', '012345678901', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Trần Thị Bình', '0987654321', 'binh.tran@example.com', '098765432109', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Lê Hoàng Cường', '0905112233', 'cuong.le@example.com', '036123456789', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Seed data for Bookings
INSERT INTO bookings (guest_id, room_id, check_in_date, check_out_date, total_price, status, note, created_at, updated_at) VALUES
(1, 3, '2026-08-01 14:00:00', '2026-08-05 12:00:00', 7200000, 'CHECKED_IN', 'Khách hàng yêu cầu nhận phòng sớm.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 4, '2026-08-10 14:00:00', '2026-08-12 12:00:00', 9000000, 'CONFIRMED', 'Đưa đón tận sân bay.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
