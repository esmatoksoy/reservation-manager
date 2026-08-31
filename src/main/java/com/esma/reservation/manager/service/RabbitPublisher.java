package com.esma.reservation.manager.service;

import com.esma.reservation.manager.dto.EmailMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange:reservation_exchange}")
    private String exchangeName;

    @Value("${app.rabbitmq.routing-key:reservation_routing_key}")
    private String routingKey;

    public void publishReservationFormLink(String toEmail, String reservationNumber) {
        EmailMessageDto message = EmailMessageDto.builder()
                .emailType(EmailMessageDto.EmailType.RESERVATION_FORM_LINK)
                .toEmail(toEmail)
                .reservationNumber(reservationNumber)
                .build();
        send(message);
    }

    public void publishDetailedReservationEmail(String toEmail, String reservationNumber) {
        EmailMessageDto message = EmailMessageDto.builder()
                .emailType(EmailMessageDto.EmailType.DETAILED_RESERVATION)
                .toEmail(toEmail)
                .reservationNumber(reservationNumber)
                .build();
        send(message);
    }

    private void send(EmailMessageDto message) {
        log.info("Publishing email message to RabbitMQ: type={}, to={}, reservationNumber={}",
                message.getEmailType(), message.getToEmail(), message.getReservationNumber());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
