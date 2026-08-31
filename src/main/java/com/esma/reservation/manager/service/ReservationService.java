package com.esma.reservation.manager.service;

import com.esma.reservation.manager.dto.FormDto;
import com.esma.reservation.manager.dto.NewReservationDto;
import com.esma.reservation.manager.dto.NewGuestDto;
import com.esma.reservation.manager.dto.ReservationDetailResponseDto;
import com.esma.reservation.manager.dto.ReservationGuestDetailDto;
import com.esma.reservation.manager.exception.ReservationNotFoundException;
import com.esma.reservation.manager.model.entity.Reservation;
import com.esma.reservation.manager.model.entity.ReservationGuest;
import com.esma.reservation.manager.model.entity.ReservationGuestDetail;
import com.esma.reservation.manager.model.type.ReservationStatus;
import com.esma.reservation.manager.model.entity.Customer;
import com.esma.reservation.manager.model.entity.Room;
import com.esma.reservation.manager.repository.ReservationGuestDetailsRepository;
import com.esma.reservation.manager.repository.ReservationGuestRepository;
import com.esma.reservation.manager.repository.ReservationRepository;
import com.esma.reservation.manager.repository.CustomerRepository;
import com.esma.reservation.manager.repository.RoomRepository;
import com.esma.reservation.manager.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationGuestDetailsRepository guestDetailsRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationGuestRepository reservationGuestRepository;
    private final ReservationMapper reservationMapper;
    private final RabbitPublisher rabbitPublisher;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;



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
    dto.setRoomId(reservation.getReservationGuests().get(0).getRoom().getId().longValue());
    dto.setNumberOfGuests(reservation.getReservationGuests().size());
    dto.setGuestDetails(guestDetails);

    return dto;
}

    @Transactional
    public void saveForm(FormDto formDto, boolean sendEmail) {
        if (formDto.getGuests() == null || formDto.getGuests().isEmpty()) {
            throw new IllegalArgumentException("No guest details provided");
        }

        Long guestId = formDto.getGuests().get(0).getGuestId();
        ReservationGuest firstGuest = reservationGuestRepository.findById(guestId)
                .orElseThrow(() -> new ReservationNotFoundException("Guest not found"));

        Reservation reservation = firstGuest.getReservation();

        Long newRoomId = formDto.getGuests().get(0).getRoomId();
        if (newRoomId != null) {
            Long currentRoomId = firstGuest.getRoom() != null ? firstGuest.getRoom().getId() : null;

            if (currentRoomId == null || !newRoomId.equals(currentRoomId)) {
                Room newRoom = roomRepository.findById(newRoomId)
                        .orElseThrow(() -> new RuntimeException("Room not found: " + newRoomId));

                log.info("Room change detected. Updating room for ALL {} guests in reservation {} to room {}",
                    reservation.getReservationGuests().size(),
                    reservation.getReservationNumber(),
                    newRoom.getRoomNumber());

                for (ReservationGuest guest : reservation.getReservationGuests()) {
                    guest.setRoom(newRoom);
                    reservationGuestRepository.save(guest);
                    log.info("Updated room for guest {} to room {}", guest.getId(), newRoom.getRoomNumber());
                }
            }
        }

        for (ReservationGuestDetailDto guestDto : formDto.getGuests()) {
            try {
                ReservationGuest guest = reservationGuestRepository.findById(guestDto.getGuestId())
                        .orElseThrow(() -> new ReservationNotFoundException("Guest not found"));

                ReservationGuestDetail detail = guestDetailsRepository.findByReservationGuestId(guest.getId())
                        .orElse(null);

                if (detail == null) {
                    detail = new ReservationGuestDetail();
                    detail.setReservationGuest(guest);
                } else {
                    log.info("Found existing ReservationGuestDetail with ID {} for guest {}", detail.getId(), guest.getId());
                }

                detail.setExpectedArrival(guestDto.getExpectedArrival());
                detail.setRoomPreferences(guestDto.getRoomPreferences());
                detail.setExtraNeeds(guestDto.getExtraNeeds());
                detail.setSpecialRequests(guestDto.getSpecialRequests());
                detail.setAllergies(guestDto.getAllergies());

                guestDetailsRepository.save(detail);
                log.info("Saved guest detail for guest: {}", guest.getId());

            } catch (Exception e) {
                log.error("Error processing guest {}: {}", guestDto.getGuestId(), e.getMessage(), e);
                throw new RuntimeException("Failed to save guest details: " + e.getMessage(), e);
            }
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
        log.info("Reservation status changed to CONFIRMED: {}", reservation.getReservationNumber());

        if (sendEmail) {// Only send email if admin chose to send it
            rabbitPublisher.publishDetailedReservationEmail(
                    reservation.getCustomer().getEmail(), reservation.getReservationNumber());
            log.info("Detailed confirmation email queued via RabbitMQ for reservation: {}", reservation.getReservationNumber());
        } else {
            log.info("Email NOT sent (admin chose 'Save' only) for reservation: {}", reservation.getReservationNumber());
        }
    }
public List<Reservation> getReservationsByCustomerId(Long customerId) {
    return reservationRepository.findByCustomerIdOrderByBookedDateDesc(customerId);
}
    public Customer getCustomerByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Customer not found with phone: " + phoneNumber));
    }

    @Transactional
    public void updateGuestDetail(ReservationGuestDetailDto guestDto) {
        ReservationGuest guest = reservationGuestRepository.findById(guestDto.getGuestId())
                .orElseThrow(() -> new ReservationNotFoundException("Guest not found"));

        log.info("Updating guest detail for guest ID: {}", guest.getId());

        if (guestDto.getRoomId() != null) {
            Long currentRoomIdLong = guest.getRoom() != null ? guest.getRoom().getId() : null;
            Long requestedRoomIdLong = guestDto.getRoomId();

            if (!requestedRoomIdLong.equals(currentRoomIdLong)) {
                Room newRoom = roomRepository.findById(requestedRoomIdLong)
                        .orElseThrow(() -> new RuntimeException("Room not found: " + requestedRoomIdLong));

                Reservation reservation = guest.getReservation();
                log.info("Updating room for all {} guests in reservation {}",
                    reservation.getReservationGuests().size(),
                    reservation.getReservationNumber());

                for (ReservationGuest reservationGuest : reservation.getReservationGuests()) {
                    reservationGuest.setRoom(newRoom);
                    reservationGuestRepository.save(reservationGuest);
                }
            }
        }

        ReservationGuestDetail detail = guestDetailsRepository.findByReservationGuestId(guest.getId())
                .orElse(null);

        if (detail == null) {
            detail = new ReservationGuestDetail();
            detail.setReservationGuest(guest);
        }

        detail.setExpectedArrival(guestDto.getExpectedArrival());
        detail.setRoomPreferences(guestDto.getRoomPreferences());
        detail.setExtraNeeds(guestDto.getExtraNeeds());
        detail.setSpecialRequests(guestDto.getSpecialRequests());
        detail.setAllergies(guestDto.getAllergies());
        detail.setAllergiesOther(guestDto.getAllergiesOther());

        guestDetailsRepository.save(detail);

        log.info("Successfully updated guest detail for guest: {}", guest.getId());
    }

    @Transactional
    public String createReservation(NewReservationDto dto) {
        log.info("Creating new reservation for email: {}", dto.getEmail());

        Customer customer = customerRepository.findByEmail(dto.getEmail())
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setEmail(dto.getEmail());
                    newCustomer.setFirstName(dto.getFirstName());
                    newCustomer.setLastName(dto.getLastName());
                    newCustomer.setPhoneNumber(dto.getPhoneNumber());
                    Customer savedCustomer = customerRepository.save(newCustomer);
                    log.info("Created new customer with email: {}", dto.getEmail());
                    return savedCustomer;
                });

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setReservationNumber(java.util.UUID.randomUUID().toString());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setBookedDate(dto.getArrivalDate());
        reservation = reservationRepository.save(reservation);
        log.info("Created reservation with ID: {} and number: {}", reservation.getId(), reservation.getReservationNumber());

        if (dto.getGuests() != null && !dto.getGuests().isEmpty()) {
            for (NewGuestDto guestDto : dto.getGuests()) {
                ReservationGuest guest = new ReservationGuest();
                guest.setReservation(reservation);
                guest.setFirstName(guestDto.getFirstName());
                guest.setLastName(guestDto.getLastName());
                guest.setGender(guestDto.getGender());
                guest.setAge(guestDto.getAge());

                if (guestDto.getRoomId() != null) {
                    Room room = roomRepository.findById(guestDto.getRoomId())
                            .orElseThrow(() -> new RuntimeException("Room not found: " + guestDto.getRoomId()));
                    guest.setRoom(room);
                }

                guest = reservationGuestRepository.save(guest);
                log.info("Created guest with ID: {}", guest.getId());

                ReservationGuestDetail detail = new ReservationGuestDetail();
                detail.setReservationGuest(guest);
                detail.setExpectedArrival(guestDto.getExpectedArrival());
                detail.setRoomPreferences(guestDto.getRoomPreferences());
                detail.setExtraNeeds(guestDto.getExtraNeeds());
                detail.setSpecialRequests(guestDto.getSpecialRequests());
                detail.setAllergies(guestDto.getAllergies());
                detail.setAllergiesOther(guestDto.getAllergiesOther());
                guestDetailsRepository.save(detail);
                log.info("Created guest detail for guest ID: {}", guest.getId());
            }
        }

        if (dto.isSendEmail()) {
            rabbitPublisher.publishReservationFormLink(customer.getEmail(), reservation.getReservationNumber());
            log.info("Reservation form link email queued via RabbitMQ for reservation: {}", reservation.getReservationNumber());
        } else {
            log.info("Email NOT sent for new reservation: {}", reservation.getReservationNumber());
        }

        return reservation.getReservationNumber();
    }

}