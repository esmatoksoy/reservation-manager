package com.esma.reservation.manager.controller;

import com.esma.reservation.manager.dto.FormDto;
import com.esma.reservation.manager.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.esma.reservation.manager.dto.ReservationDetailResponseDto;
import com.esma.reservation.manager.exception.ReservationNotFoundException;

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
            reservationService.saveForm(formDto);
            return ResponseEntity.ok("Reservation saved ");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save reservation: " + ex.getMessage());
        }
    }
    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<String> handleReservationNotFound(ReservationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}