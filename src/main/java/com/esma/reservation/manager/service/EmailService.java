package com.esma.reservation.manager.service;

import com.esma.reservation.manager.model.entity.Reservation;

public interface EmailService {
    void sendReservationFormLink(String toEmail, String reservationNumber);
    void sendConfirmationEmail(String toEmail, String reservationNumber);
    void sendDetailedReservationEmail(Reservation reservation);
}