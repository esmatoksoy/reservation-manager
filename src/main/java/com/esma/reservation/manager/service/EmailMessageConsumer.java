package com.esma.reservation.manager.service;

import com.esma.reservation.manager.dto.EmailMessageDto;
import com.esma.reservation.manager.exception.ReservationNotFoundException;
import com.esma.reservation.manager.model.entity.Reservation;
import com.esma.reservation.manager.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailMessageConsumer {

    private final EmailService emailService;
    private final ReservationRepository reservationRepository;
    private final TelegramNotificationService telegramNotificationService;

    @RabbitListener(queues = "${app.rabbitmq.queue:reservation_mail_queue}")
    public void consume(EmailMessageDto message) {
        log.info("Received email message from RabbitMQ: type={}, to={}, reservationNumber={}",
                message.getEmailType(), message.getToEmail(), message.getReservationNumber());

        try {
            switch (message.getEmailType()) {
                case RESERVATION_FORM_LINK -> {
                    emailService.sendReservationFormLink(message.getToEmail(), message.getReservationNumber());
                    telegramNotificationService.sendNotification(
                        "✉️ <b>Form Link Email Sent</b>\n" +
                        "👤 To: <code>" + message.getToEmail() + "</code>\n" +
                        "📋 Reservation: <code>" + message.getReservationNumber() + "</code>"
                    );
                }

                case CONFIRMATION -> {
                    emailService.sendConfirmationEmail(message.getToEmail(), message.getReservationNumber());
                    telegramNotificationService.sendNotification(
                        "✅ <b>Confirmation Email Sent</b>\n" +
                        "👤 To: <code>" + message.getToEmail() + "</code>\n" +
                        "📋 Reservation: <code>" + message.getReservationNumber() + "</code>"
                    );
                }

                case DETAILED_RESERVATION -> {
                    Reservation reservation = reservationRepository
                            .findByReservationNumber(message.getReservationNumber())
                            .orElseThrow(() -> new ReservationNotFoundException(
                                    "Reservation not found: " + message.getReservationNumber()));
                    emailService.sendDetailedReservationEmail(reservation);
                    telegramNotificationService.sendNotification(
                        "📄 <b>Detailed Reservation Email Sent</b>\n" +
                        "👤 To: <code>" + message.getToEmail() + "</code>\n" +
                        "📋 Reservation: <code>" + message.getReservationNumber() + "</code>\n" +
                        "📌 Status: <b>" + reservation.getStatus() + "</b>"
                    );
                }

                default -> log.warn("Unknown email type: {}", message.getEmailType());
            }
        } catch (Exception e) {
            log.error("Failed to process email message: type={}, reservationNumber={}, error={}",
                    message.getEmailType(), message.getReservationNumber(), e.getMessage(), e);

            telegramNotificationService.sendNotification(
                "🚨 <b>Email Failed!</b>\n" +
                "Type: " + message.getEmailType() + "\n" +
                "To: <code>" + message.getToEmail() + "</code>\n" +
                "Reservation: <code>" + message.getReservationNumber() + "</code>\n" +
                "Error: " + e.getMessage()
            );

            // Rethrow so Spring AMQP retry/dead-letter logic kicks in.
            // The message will be retried up to the configured limit
            // (spring.rabbitmq.listener.simple.retry.*) then moved to the DLQ.
            throw new org.springframework.amqp.AmqpRejectAndDontRequeueException(
                    "Permanently failed to process message for reservation: " + message.getReservationNumber(), e);
        }
    }
}
