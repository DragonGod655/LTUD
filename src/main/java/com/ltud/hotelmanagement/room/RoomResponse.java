package com.ltud.hotelmanagement.room;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RoomResponse {
    private Long id;
    private String roomNumber;
    private RoomType roomType;
    private String description;
    private BigDecimal pricePerNight;
    private RoomStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RoomResponse() {
    }

    public RoomResponse(Long id, String roomNumber, RoomType roomType, String description, BigDecimal pricePerNight, RoomStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RoomResponse fromEntity(Room room) {
        if (room == null) return null;
        return new Builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .description(room.getDescription())
                .pricePerNight(room.getPricePerNight())
                .status(room.getStatus())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }

    public static class Builder {
        private Long id;
        private String roomNumber;
        private RoomType roomType;
        private String description;
        private BigDecimal pricePerNight;
        private RoomStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder roomNumber(String roomNumber) { this.roomNumber = roomNumber; return this; }
        public Builder roomType(RoomType roomType) { this.roomType = roomType; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder pricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; return this; }
        public Builder status(RoomStatus status) { this.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public RoomResponse build() {
            return new RoomResponse(id, roomNumber, roomType, description, pricePerNight, status, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public String getRoomNumber() { return roomNumber; }
    public RoomType getRoomType() { return roomType; }
    public String getDescription() { return description; }
    public BigDecimal getPricePerNight() { return pricePerNight; }
    public RoomStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
