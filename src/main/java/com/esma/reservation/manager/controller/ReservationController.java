package com.esma.reservation.manager.controller;

import java.util.List;
import java.util.stream.Collectors;
import com.esma.reservation.manager.model.entity.Reservation;
import com.esma.reservation.manager.model.entity.Customer;
import com.esma.reservation.manager.dto.FormDto;
import com.esma.reservation.manager.dto.NewReservationDto;
import com.esma.reservation.manager.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.esma.reservation.manager.dto.ReservationDetailResponseDto;
import com.esma.reservation.manager.exception.ReservationNotFoundException;
import com.esma.reservation.manager.dto.CustomerDto;
import com.esma.reservation.manager.dto.ReservationSummaryDto;
import com.esma.reservation.manager.dto.ReservationGuestDetailDto;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/guest-detail/{requestId}")
    public ResponseEntity<ReservationDetailResponseDto> getReservation(@PathVariable String requestId) {
        ReservationDetailResponseDto form = reservationService.findByReservationNumber(requestId);
        return ResponseEntity.ok(form);
    }

    @PostMapping("/guest-detail")
    public ResponseEntity<String> saveReservation(@RequestBody FormDto formDto) {
        try {
            reservationService.saveForm(formDto, formDto.isSendEmail());
            return ResponseEntity.ok("Reservation saved");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save reservation: " + ex.getMessage());
        }
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<String> handleReservationNotFound(ReservationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @GetMapping("/api/customers/phone/{phoneNumber}")
    public ResponseEntity<CustomerDto> getCustomerByPhone(@PathVariable String phoneNumber) {
        try {
            Customer customer = reservationService.getCustomerByPhoneNumber(phoneNumber);
            CustomerDto dto = new CustomerDto();
            dto.setId(customer.getId());
            dto.setEmail(customer.getEmail());
            dto.setFirstName(customer.getFirstName());
            dto.setLastName(customer.getLastName());
            dto.setPhoneNumber(customer.getPhoneNumber());
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/reservations/customer/{customerId}")
    public ResponseEntity<List<ReservationSummaryDto>> getReservationsByCustomer(@PathVariable Long customerId) {
        List<Reservation> reservations = reservationService.getReservationsByCustomerId(customerId);
        List<ReservationSummaryDto> dtos = reservations.stream().map(r -> {
            ReservationSummaryDto rd = new ReservationSummaryDto();
            rd.setId(r.getId());
            rd.setReservationNumber(r.getReservationNumber());
            rd.setBookedDate(r.getBookedDate());
            rd.setCreatedAt(r.getBookedDate());
            rd.setStatus(r.getStatus() != null ? r.getStatus().name() : null);
            rd.setCustomerId(r.getCustomer() != null ? r.getCustomer().getId() : null);
            return rd;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/api/guest-detail/{guestId}")
    public ResponseEntity<String> updateGuestDetail(@PathVariable Long guestId, @RequestBody ReservationGuestDetailDto guestDto) {
        try {
            guestDto.setGuestId(guestId);
            reservationService.updateGuestDetail(guestDto);
            return ResponseEntity.ok("Guest detail updated successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update guest detail: " + ex.getMessage());
        }
    }

    @PostMapping("/api/reservations")
    public ResponseEntity<String> createReservation(@RequestBody NewReservationDto dto) {
        try {
            String requestId = reservationService.createReservation(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(requestId);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create reservation: " + ex.getMessage());
        }
    }
}