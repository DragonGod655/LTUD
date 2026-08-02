package com.ltud.hotelmanagement.service;

import com.ltud.hotelmanagement.exception.DuplicateResourceException;
import com.ltud.hotelmanagement.exception.ResourceNotFoundException;
import com.ltud.hotelmanagement.room.Room;
import com.ltud.hotelmanagement.room.RoomRepository;
import com.ltud.hotelmanagement.room.RoomRequest;
import com.ltud.hotelmanagement.room.RoomResponse;
import com.ltud.hotelmanagement.room.RoomService;
import com.ltud.hotelmanagement.room.RoomStatus;
import com.ltud.hotelmanagement.room.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    private Room room;
    private RoomRequest roomRequest;

    @BeforeEach
    void setUp() {
        room = new Room("P101", RoomType.SINGLE, "Phòng đơn tiêu chuẩn", BigDecimal.valueOf(500000), RoomStatus.AVAILABLE);
        room.setId(1L);

        roomRequest = new RoomRequest();
        roomRequest.setRoomNumber("P101");
        roomRequest.setRoomType(RoomType.SINGLE);
        roomRequest.setDescription("Phòng đơn tiêu chuẩn");
        roomRequest.setPricePerNight(BigDecimal.valueOf(500000));
        roomRequest.setStatus(RoomStatus.AVAILABLE);
    }

    @Test
    void getRoomById_Success() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        RoomResponse response = roomService.getRoomById(1L);

        assertNotNull(response);
        assertEquals("P101", response.getRoomNumber());
        assertEquals(RoomType.SINGLE, response.getRoomType());
    }

    @Test
    void getRoomById_NotFound_ThrowsException() {
        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roomService.getRoomById(99L));
    }

    @Test
    void createRoom_DuplicateRoomNumber_ThrowsException() {
        when(roomRepository.existsByRoomNumber("P101")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> roomService.createRoom(roomRequest));
        verify(roomRepository, never()).save(any());
    }

    @Test
    void createRoom_Success() {
        when(roomRepository.existsByRoomNumber("P101")).thenReturn(false);
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        RoomResponse response = roomService.createRoom(roomRequest);

        assertNotNull(response);
        assertEquals("P101", response.getRoomNumber());
        verify(roomRepository, times(1)).save(any(Room.class));
    }
}
