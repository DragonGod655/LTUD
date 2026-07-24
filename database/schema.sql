-- Hotel Management Database Schema

CREATE TABLE rooms (
    id BIGSERIAL PRIMARY KEY,
    room_number VARCHAR(30) NOT NULL UNIQUE,
    room_type VARCHAR(30) NOT NULL CHECK (room_type IN ('SINGLE', 'DOUBLE', 'SUITE', 'VIP')),
    description VARCHAR(1000),
    price_per_night NUMERIC(12,2) NOT NULL CHECK (price_per_night > 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('AVAILABLE', 'OCCUPIED', 'MAINTENANCE')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE guests (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(180) UNIQUE,
    identity_card VARCHAR(30) UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    guest_id BIGINT NOT NULL REFERENCES guests(id) ON DELETE CASCADE,
    room_id BIGINT NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    check_in TIMESTAMP NOT NULL,
    check_out TIMESTAMP NOT NULL,
    total_price NUMERIC(12,2) NOT NULL CHECK (total_price >= 0),
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED' CHECK (status IN ('CONFIRMED', 'CHECKED_IN', 'CHECKED_OUT', 'CANCELLED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_rooms_status ON rooms(status);
CREATE INDEX idx_rooms_type ON rooms(room_type);
CREATE INDEX idx_bookings_dates ON bookings(check_in, check_out);
