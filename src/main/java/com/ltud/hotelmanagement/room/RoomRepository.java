package com.ltud.hotelmanagement.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByRoomNumber(String roomNumber);
    boolean existsByRoomNumberAndIdNot(String roomNumber, Long id);

    Page<Room> findByRoomNumberContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String roomNumber, String description, Pageable pageable);
    Page<Room> findByStatus(RoomStatus status, Pageable pageable);
}
