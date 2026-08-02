package com.ltud.hotelmanagement.room;

import com.ltud.hotelmanagement.common.ApiResponse;
import com.ltud.hotelmanagement.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Room Management", description = "APIs quản lý thông tin & trạng thái phòng khách sạn")
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "Lấy danh sách các phòng (Có Phân trang, Tìm kiếm & Lọc theo Trạng thái)")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<RoomResponse>>> getAllRooms(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) RoomStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        PageResponse<RoomResponse> rooms = roomService.getAllRooms(search, status, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(rooms));
    }

    @Operation(summary = "Lấy thông tin chi tiết một phòng theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomResponse>> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(roomService.getRoomById(id)));
    }

    @Operation(summary = "Thêm mới phòng khách sạn")
    @PostMapping
    public ResponseEntity<ApiResponse<RoomResponse>> createRoom(@Valid @RequestBody RoomRequest request) {
        RoomResponse response = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Thêm phòng mới thành công", response));
    }

    @Operation(summary = "Cập nhật thông tin phòng khách sạn theo ID")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomResponse>> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomRequest request) {
        RoomResponse response = roomService.updateRoom(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thông tin phòng thành công", response));
    }

    @Operation(summary = "Xóa thông tin phòng khỏi hệ thống")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa phòng thành công", null));
    }
}
