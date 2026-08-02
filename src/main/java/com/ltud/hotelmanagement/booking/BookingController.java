package com.ltud.hotelmanagement.booking;

import com.ltud.hotelmanagement.booking.dto.BookingRequest;
import com.ltud.hotelmanagement.booking.dto.BookingResponse;
import com.ltud.hotelmanagement.common.ApiResponse;
import com.ltud.hotelmanagement.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Booking Management", description = "APIs đặt phòng, nhận phòng & trả phòng khách sạn")
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(summary = "Lấy danh sách đơn đặt phòng (Phân trang & Lọc theo Trạng thái)")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BookingResponse>>> getAllBookings(
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        PageResponse<BookingResponse> bookings = bookingService.getAllBookings(status, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    @Operation(summary = "Lấy chi tiết đơn đặt phòng theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.getBookingById(id)));
    }

    @Operation(summary = "Tạo mới đơn đặt phòng")
    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(@Valid @RequestBody BookingRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Tạo đơn đặt phòng thành công", response));
    }

    @Operation(summary = "Làm thủ tục nhận phòng (Check-in)")
    @PutMapping("/{id}/check-in")
    public ResponseEntity<ApiResponse<BookingResponse>> checkIn(@PathVariable Long id) {
        BookingResponse response = bookingService.checkIn(id);
        return ResponseEntity.ok(ApiResponse.success("Nhận phòng thành công", response));
    }

    @Operation(summary = "Làm thủ tục trả phòng (Check-out)")
    @PutMapping("/{id}/check-out")
    public ResponseEntity<ApiResponse<BookingResponse>> checkOut(@PathVariable Long id) {
        BookingResponse response = bookingService.checkOut(id);
        return ResponseEntity.ok(ApiResponse.success("Trả phòng thành công", response));
    }

    @Operation(summary = "Hủy đơn đặt phòng")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(@PathVariable Long id) {
        BookingResponse response = bookingService.cancelBooking(id);
        return ResponseEntity.ok(ApiResponse.success("Hủy đơn đặt phòng thành công", response));
    }
}
