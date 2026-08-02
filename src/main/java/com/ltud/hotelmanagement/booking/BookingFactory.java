package com.ltud.hotelmanagement.booking;

import com.ltud.hotelmanagement.exception.BadRequestException;
import com.ltud.hotelmanagement.guest.Guest;
import com.ltud.hotelmanagement.room.Room;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Factory Pattern implementation for creating Booking entities with total price calculation and validation logic.
 */
@Component
public class BookingFactory {

    public Booking createBooking(Guest guest, Room room, LocalDateTime checkInDate, LocalDateTime checkOutDate, String note) {
        if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
            throw new BadRequestException("Ngày trả phòng phải sau ngày nhận phòng");
        }

        long hours = Duration.between(checkInDate, checkOutDate).toHours();
        long nights = Math.max(1, (long) Math.ceil(hours / 24.0));

        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        return new Booking(
                guest,
                room,
                checkInDate,
                checkOutDate,
                totalPrice,
                BookingStatus.CONFIRMED,
                note
        );
    }
}
