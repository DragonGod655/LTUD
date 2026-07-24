package com.ltud.hotelmanagement.room;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms(String search) {
        List<Room> rooms;
        if (search != null && !search.trim().isEmpty()) {
            rooms = roomRepository.findByRoomNumberContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search.trim(), search.trim());
        } else {
            rooms = roomRepository.findAll();
        }
        return rooms.stream().map(RoomResponse::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng với ID: " + id));
        return RoomResponse.fromEntity(room);
    }

    @Transactional
    public RoomResponse createRoom(RoomRequest request) {
        if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số phòng " + request.getRoomNumber() + " đã tồn tại trong hệ thống");
        }

        Room room = new Room(
                request.getRoomNumber(),
                request.getRoomType(),
                request.getDescription(),
                request.getPricePerNight(),
                request.getStatus() != null ? request.getStatus() : RoomStatus.AVAILABLE
        );

        Room savedRoom = roomRepository.save(room);
        return RoomResponse.fromEntity(savedRoom);
    }

    @Transactional
    public RoomResponse updateRoom(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng với ID: " + id));

        if (roomRepository.existsByRoomNumberAndIdNot(request.getRoomNumber(), id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số phòng " + request.getRoomNumber() + " đã trùng với phòng khác");
        }

        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(request.getRoomType());
        room.setDescription(request.getDescription());
        room.setPricePerNight(request.getPricePerNight());
        if (request.getStatus() != null) {
            room.setStatus(request.getStatus());
        }

        Room updatedRoom = roomRepository.save(room);
        return RoomResponse.fromEntity(updatedRoom);
    }

    @Transactional
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng với ID: " + id);
        }
        roomRepository.deleteById(id);
    }
}
