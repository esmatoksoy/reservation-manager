package com.esma.reservation.manager.service;

import com.esma.reservation.manager.dto.FormDto;
import com.esma.reservation.manager.dto.ReservationDetailResponseDto;
import com.esma.reservation.manager.dto.ReservationGuestDetailDto;
import com.esma.reservation.manager.exception.ReservationNotFoundException;
import com.esma.reservation.manager.model.entity.Reservation;
import com.esma.reservation.manager.model.entity.ReservationGuest;
import com.esma.reservation.manager.model.entity.ReservationGuestDetail;
import com.esma.reservation.manager.repository.ReservationGuestDetailsRepository;
import com.esma.reservation.manager.repository.ReservationGuestRepository;
import com.esma.reservation.manager.repository.ReservationRepository;
import com.esma.reservation.manager.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationGuestDetailsRepository guestDetailsRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationGuestRepository reservationGuestRepository;
    private final ReservationMapper reservationMapper;


    public ReservationDetailResponseDto findByReservationNumber(String requestId) {
    Reservation reservation = reservationRepository.findByReservationNumber(requestId)
            .orElseThrow(() -> new ReservationNotFoundException(
                    "Reservation not found with requestId: " + requestId));

     List<ReservationGuestDetailDto> guestDetails =
                    reservationMapper.toGuestDetailDtos(reservation.getReservationGuests());

    ReservationDetailResponseDto dto = new ReservationDetailResponseDto();
    dto.setRequestId(reservation.getReservationNumber());
    dto.setGuestName(reservation.getCustomer().getFirstName() + " " + reservation.getCustomer().getLastName());
    dto.setCheckInDate(reservation.getBookedDate().toString());
    dto.setRoomType(reservation.getReservationGuests().get(0).getRoom().getRoomType());
    dto.setNumberOfGuests(reservation.getReservationGuests().size());
    dto.setGuestDetails(guestDetails);

    return dto;
}

    @Transactional
    public void saveForm(FormDto formDto) {
        if (formDto == null || formDto.getGuests() == null) {
            throw new IllegalArgumentException("Form data or guests cannot be null");
        }

        for (ReservationGuestDetailDto guestDto : formDto.getGuests()) {
            if (guestDto.getGuestId() == null) {
                throw new IllegalArgumentException("Guest ID cannot be null");
            }

            ReservationGuest guest = reservationGuestRepository.findById(guestDto.getGuestId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Guest not found with ID: " + guestDto.getGuestId()));

            ReservationGuestDetail entity = guest.getReservationGuestDetail();

            if (entity == null) {
                entity = new ReservationGuestDetail();
            }

            entity.setExpectedArrival(guestDto.getExpectedArrival());
            entity.setSpecialRequests(guestDto.getSpecialRequests());
            entity.setAllergies(guestDto.getAllergies());
            entity.setAllergiesOther(guestDto.getAllergiesOther());
            entity.setRoomPreferences(guestDto.getRoomPreferences());
            entity.setExtraNeeds(guestDto.getExtraNeeds());
            entity.setReservationGuest(guest);

            guest.setReservationGuestDetail(entity);
            reservationGuestRepository.save(guest);
        }
    }

}