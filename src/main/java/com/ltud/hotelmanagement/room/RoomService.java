package com.ltud.hotelmanagement.room;

import com.ltud.hotelmanagement.common.PageResponse;
import com.ltud.hotelmanagement.exception.DuplicateResourceException;
import com.ltud.hotelmanagement.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<RoomResponse> getAllRooms(String search, RoomStatus status, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Room> roomPage;
        if (status != null) {
            roomPage = roomRepository.findByStatus(status, pageable);
        } else if (search != null && !search.trim().isEmpty()) {
            roomPage = roomRepository.findByRoomNumberContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search.trim(), search.trim(), pageable);
        } else {
            roomPage = roomRepository.findAll(pageable);
        }

        Page<RoomResponse> responsePage = roomPage.map(RoomResponse::fromEntity);
        return PageResponse.from(responsePage);
    }

    @Transactional(readOnly = true)
    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng với ID: " + id));
        return RoomResponse.fromEntity(room);
    }

    @Transactional
    public RoomResponse createRoom(RoomRequest request) {
        if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new DuplicateResourceException("Số phòng " + request.getRoomNumber() + " đã tồn tại trong hệ thống");
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
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng với ID: " + id));

        if (roomRepository.existsByRoomNumberAndIdNot(request.getRoomNumber(), id)) {
            throw new DuplicateResourceException("Số phòng " + request.getRoomNumber() + " đã trùng với phòng khác");
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
            throw new ResourceNotFoundException("Không tìm thấy phòng với ID: " + id);
        }
        roomRepository.deleteById(id);
    }
}
