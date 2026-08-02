package com.ltud.hotelmanagement.guest;

import com.ltud.hotelmanagement.common.PageResponse;
import com.ltud.hotelmanagement.exception.DuplicateResourceException;
import com.ltud.hotelmanagement.exception.ResourceNotFoundException;
import com.ltud.hotelmanagement.guest.dto.GuestRequest;
import com.ltud.hotelmanagement.guest.dto.GuestResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<GuestResponse> getAllGuests(String search, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Guest> guestPage;
        if (search != null && !search.trim().isEmpty()) {
            guestPage = guestRepository.findByFullNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(search.trim(), search.trim(), pageable);
        } else {
            guestPage = guestRepository.findAll(pageable);
        }

        Page<GuestResponse> responsePage = guestPage.map(GuestResponse::fromEntity);
        return PageResponse.from(responsePage);
    }

    @Transactional(readOnly = true)
    public GuestResponse getGuestById(Long id) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + id));
        return GuestResponse.fromEntity(guest);
    }

    @Transactional
    public GuestResponse createGuest(GuestRequest request) {
        if (guestRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Số điện thoại " + request.getPhone() + " đã tồn tại trong hệ thống");
        }

        Guest guest = new Guest(
                request.getFullName(),
                request.getPhone(),
                request.getEmail(),
                request.getIdentityCard()
        );

        Guest savedGuest = guestRepository.save(guest);
        return GuestResponse.fromEntity(savedGuest);
    }

    @Transactional
    public GuestResponse updateGuest(Long id, GuestRequest request) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + id));

        if (guestRepository.existsByPhoneAndIdNot(request.getPhone(), id)) {
            throw new DuplicateResourceException("Số điện thoại " + request.getPhone() + " đã thuộc về khách hàng khác");
        }

        guest.setFullName(request.getFullName());
        guest.setPhone(request.getPhone());
        guest.setEmail(request.getEmail());
        guest.setIdentityCard(request.getIdentityCard());

        Guest updatedGuest = guestRepository.save(guest);
        return GuestResponse.fromEntity(updatedGuest);
    }

    @Transactional
    public void deleteGuest(Long id) {
        if (!guestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + id);
        }
        guestRepository.deleteById(id);
    }
}
