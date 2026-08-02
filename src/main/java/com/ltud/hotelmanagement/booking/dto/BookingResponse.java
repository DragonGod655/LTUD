package com.ltud.hotelmanagement.booking.dto;

import com.ltud.hotelmanagement.booking.Booking;
import com.ltud.hotelmanagement.booking.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingResponse {
    private Long id;
    private Long guestId;
    private String guestName;
    private String guestPhone;
    private Long roomId;
    private String roomNumber;
    private String roomType;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private BigDecimal totalPrice;
    private BookingStatus status;
    private String note;
    private LocalDateTime createdAt;

    public BookingResponse() {
    }

    public BookingResponse(Long id, Long guestId, String guestName, String guestPhone, Long roomId, String roomNumber, String roomType, LocalDateTime checkInDate, LocalDateTime checkOutDate, BigDecimal totalPrice, BookingStatus status, String note, LocalDateTime createdAt) {
        this.id = id;
        this.guestId = guestId;
        this.guestName = guestName;
        this.guestPhone = guestPhone;
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.note = note;
        this.createdAt = createdAt;
    }

    public static BookingResponse fromEntity(Booking booking) {
        if (booking == null) return null;
        return new Builder()
                .id(booking.getId())
                .guestId(booking.getGuest().getId())
                .guestName(booking.getGuest().getFullName())
                .guestPhone(booking.getGuest().getPhone())
                .roomId(booking.getRoom().getId())
                .roomNumber(booking.getRoom().getRoomNumber())
                .roomType(booking.getRoom().getRoomType().name())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .note(booking.getNote())
                .createdAt(booking.getCreatedAt())
                .build();
    }

    public static class Builder {
        private Long id;
        private Long guestId;
        private String guestName;
        private String guestPhone;
        private Long roomId;
        private String roomNumber;
        private String roomType;
        private LocalDateTime checkInDate;
        private LocalDateTime checkOutDate;
        private BigDecimal totalPrice;
        private BookingStatus status;
        private String note;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder guestId(Long guestId) { this.guestId = guestId; return this; }
        public Builder guestName(String guestName) { this.guestName = guestName; return this; }
        public Builder guestPhone(String guestPhone) { this.guestPhone = guestPhone; return this; }
        public Builder roomId(Long roomId) { this.roomId = roomId; return this; }
        public Builder roomNumber(String roomNumber) { this.roomNumber = roomNumber; return this; }
        public Builder roomType(String roomType) { this.roomType = roomType; return this; }
        public Builder checkInDate(LocalDateTime checkInDate) { this.checkInDate = checkInDate; return this; }
        public Builder checkOutDate(LocalDateTime checkOutDate) { this.checkOutDate = checkOutDate; return this; }
        public Builder totalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; return this; }
        public Builder status(BookingStatus status) { this.status = status; return this; }
        public Builder note(String note) { this.note = note; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public BookingResponse build() {
            return new BookingResponse(id, guestId, guestName, guestPhone, roomId, roomNumber, roomType, checkInDate, checkOutDate, totalPrice, status, note, createdAt);
        }
    }

    public Long getId() { return id; }
    public Long getGuestId() { return guestId; }
    public String getGuestName() { return guestName; }
    public String getGuestPhone() { return guestPhone; }
    public Long getRoomId() { return roomId; }
    public String getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }
    public LocalDateTime getCheckInDate() { return checkInDate; }
    public LocalDateTime getCheckOutDate() { return checkOutDate; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public BookingStatus getStatus() { return status; }
    public String getNote() { return note; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
