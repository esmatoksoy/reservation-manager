package com.esma.reservation.manager.service;

import com.esma.reservation.manager.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange:reservation_exchange}")
    private String exchangeName;

    @Value("${app.rabbitmq.routing-key:reservation_routing_key}")
    private String routingKey;

    public void send(String message) {

        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
