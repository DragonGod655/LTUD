package com.ltud.hotelmanagement.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class BookingRequest {

    @NotNull(message = "Mã khách hàng không được để trống")
    private Long guestId;

    @NotNull(message = "Mã phòng không được để trống")
    private Long roomId;

    @NotNull(message = "Thời gian nhận phòng không được để trống")
    @FutureOrPresent(message = "Thời gian nhận phòng phải ở hiện tại hoặc tương lai")
    private LocalDateTime checkInDate;

    @NotNull(message = "Thời gian trả phòng không được để trống")
    private LocalDateTime checkOutDate;

    private String note;

    public BookingRequest() {
    }

    public BookingRequest(Long guestId, Long roomId, LocalDateTime checkInDate, LocalDateTime checkOutDate, String note) {
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.note = note;
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public LocalDateTime getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDateTime checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDateTime getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDateTime checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
