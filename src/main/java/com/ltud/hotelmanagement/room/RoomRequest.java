package com.ltud.hotelmanagement.room;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class RoomRequest {

    @NotBlank(message = "Số/mã phòng không được để trống")
    @Size(max = 30, message = "Số/mã phòng không vượt quá 30 ký tự")
    private String roomNumber;

    @NotNull(message = "Loại phòng không được để trống")
    private RoomType roomType;

    @Size(max = 1000, message = "Mô tả không vượt quá 1000 ký tự")
    private String description;

    @NotNull(message = "Giá phòng/đêm không được để trống")
    @DecimalMin(value = "0.01", message = "Giá phòng phải lớn hơn 0")
    private BigDecimal pricePerNight;

    private RoomStatus status = RoomStatus.AVAILABLE;

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}
