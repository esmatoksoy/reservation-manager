package com.esma.reservation.manager.service;

import com.esma.reservation.manager.model.entity.Reservation;
import com.esma.reservation.manager.model.type.ReservationStatus;
import com.esma.reservation.manager.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartupEmailSender {

    private final ReservationRepository reservationRepository;
    private final EmailService emailService;
    @EventListener(ApplicationReadyEvent.class)
    public void sendPendingReservationEmails() {
        log.info("Application started. Checking for PENDING reservations...");

        List<Reservation> pendingReservations = reservationRepository
            .findByStatus(ReservationStatus.PENDING);

        log.info("Found {} PENDING reservations", pendingReservations.size());

        for (Reservation reservation : pendingReservations) {
            String customerEmail = reservation.getCustomer().getEmail();
            String reservationNumber = reservation.getReservationNumber();

            try {
                emailService.sendReservationFormLink(customerEmail, reservationNumber);
                log.info("Email sent for reservation: {}", reservationNumber);
            } catch (Exception e) {
                log.error("Failed to send email for reservation: {}", reservationNumber, e);
            }
        }
    }
}