package com.ltud.hotelmanagement.guest;

import com.ltud.hotelmanagement.common.ApiResponse;
import com.ltud.hotelmanagement.common.PageResponse;
import com.ltud.hotelmanagement.guest.dto.GuestRequest;
import com.ltud.hotelmanagement.guest.dto.GuestResponse;
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

@Tag(name = "Guest Management", description = "APIs quản lý khách hàng khách sạn")
@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @Operation(summary = "Lấy danh sách khách hàng (Có Phân trang & Tìm kiếm)")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<GuestResponse>>> getAllGuests(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        PageResponse<GuestResponse> guests = guestService.getAllGuests(search, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(guests));
    }

    @Operation(summary = "Lấy thông tin chi tiết một khách hàng theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GuestResponse>> getGuestById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(guestService.getGuestById(id)));
    }

    @Operation(summary = "Thêm mới thông tin khách hàng")
    @PostMapping
    public ResponseEntity<ApiResponse<GuestResponse>> createGuest(@Valid @RequestBody GuestRequest request) {
        GuestResponse response = guestService.createGuest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Thêm khách hàng thành công", response));
    }

    @Operation(summary = "Cập nhật thông tin khách hàng")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GuestResponse>> updateGuest(@PathVariable Long id, @Valid @RequestBody GuestRequest request) {
        GuestResponse response = guestService.updateGuest(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thông tin khách hàng thành công", response));
    }

    @Operation(summary = "Xóa thông tin khách hàng khỏi hệ thống")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa khách hàng thành công", null));
    }
}
