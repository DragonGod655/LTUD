package com.ltud.hotelmanagement.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId AND b.status IN ('CONFIRMED', 'CHECKED_IN') " +
           "AND ((b.checkInDate < :checkOutDate AND b.checkOutDate > :checkInDate))")
    List<Booking> findConflictingBookings(@Param("roomId") Long roomId,
                                         @Param("checkInDate") LocalDateTime checkInDate,
                                         @Param("checkOutDate") LocalDateTime checkOutDate);

    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);
    
    List<Booking> findByGuestId(Long guestId);
    
    List<Booking> findByRoomId(Long roomId);
}
