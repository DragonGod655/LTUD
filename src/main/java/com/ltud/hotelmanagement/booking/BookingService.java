package com.ltud.hotelmanagement.booking;

import com.ltud.hotelmanagement.booking.dto.BookingRequest;
import com.ltud.hotelmanagement.booking.dto.BookingResponse;
import com.ltud.hotelmanagement.common.PageResponse;
import com.ltud.hotelmanagement.exception.BadRequestException;
import com.ltud.hotelmanagement.exception.ResourceNotFoundException;
import com.ltud.hotelmanagement.guest.Guest;
import com.ltud.hotelmanagement.guest.GuestRepository;
import com.ltud.hotelmanagement.room.Room;
import com.ltud.hotelmanagement.room.RoomRepository;
import com.ltud.hotelmanagement.room.RoomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final BookingFactory bookingFactory;

    public BookingService(BookingRepository bookingRepository,
                          GuestRepository guestRepository,
                          RoomRepository roomRepository,
                          BookingFactory bookingFactory) {
        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.bookingFactory = bookingFactory;
    }

    @Transactional(readOnly = true)
    public PageResponse<BookingResponse> getAllBookings(BookingStatus status, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Booking> bookingPage;
        if (status != null) {
            bookingPage = bookingRepository.findByStatus(status, pageable);
        } else {
            bookingPage = bookingRepository.findAll(pageable);
        }

        Page<BookingResponse> responsePage = bookingPage.map(BookingResponse::fromEntity);
        return PageResponse.from(responsePage);
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn đặt phòng với ID: " + id));
        return BookingResponse.fromEntity(booking);
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        Guest guest = guestRepository.findById(request.getGuestId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + request.getGuestId()));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng với ID: " + request.getRoomId()));

        if (room.getStatus() == RoomStatus.MAINTENANCE) {
            throw new BadRequestException("Phòng " + room.getRoomNumber() + " đang trong quá trình bảo trì, không thể đặt phòng");
        }

        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                room.getId(), request.getCheckInDate(), request.getCheckOutDate()
        );

        if (!conflicts.isEmpty()) {
            throw new BadRequestException("Phòng " + room.getRoomNumber() + " đã có lịch đặt trong khoảng thời gian được chọn");
        }

        Booking booking = bookingFactory.createBooking(
                guest, room, request.getCheckInDate(), request.getCheckOutDate(), request.getNote()
        );

        Booking savedBooking = bookingRepository.save(booking);
        return BookingResponse.fromEntity(savedBooking);
    }

    @Transactional
    public BookingResponse checkIn(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn đặt phòng với ID: " + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.CHECKED_OUT) {
            throw new BadRequestException("Không thể nhận phòng đối với đơn đặt phòng đã hủy hoặc đã trả phòng");
        }

        booking.setStatus(BookingStatus.CHECKED_IN);
        Room room = booking.getRoom();
        room.setStatus(RoomStatus.OCCUPIED);
        roomRepository.save(room);

        return BookingResponse.fromEntity(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse checkOut(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn đặt phòng với ID: " + bookingId));

        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new BadRequestException("Chỉ có thể làm thủ tục trả phòng cho đơn đặt phòng đang ở trạng thái CHECKED_IN");
        }

        booking.setStatus(BookingStatus.CHECKED_OUT);
        Room room = booking.getRoom();
        room.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room);

        return BookingResponse.fromEntity(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn đặt phòng với ID: " + bookingId));

        if (booking.getStatus() == BookingStatus.CHECKED_OUT) {
            throw new BadRequestException("Không thể hủy đơn đặt phòng đã hoàn tất trả phòng");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Room room = booking.getRoom();
        if (room.getStatus() == RoomStatus.OCCUPIED) {
            room.setStatus(RoomStatus.AVAILABLE);
            roomRepository.save(room);
        }

        return BookingResponse.fromEntity(bookingRepository.save(booking));
    }
}
