package com.esma.reservation.manager.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.queue:reservation_mail_queue}")
    private String queueName;

    @Value("${app.rabbitmq.exchange:reservation_exchange}")
    private String exchangeName;

    @Value("${app.rabbitmq.routing-key:reservation_routing_key}")
    private String routingKey; //
    //bean is a Java object managed by the Spring IoC container that defines a component of your messaging infrastructure or logic
    @Bean
    //When you define these components as beans, the RabbitAdmin (part of Spring AMQP)
    // automatically interacts with the RabbitMQ broker to declare them when the application starts or a connection is established
    public Queue reservationMailQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public DirectExchange reservationExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding reservationMailBinding(Queue reservationMailQueue, DirectExchange reservationExchange) {
        return BindingBuilder.bind(reservationMailQueue)
                .to(reservationExchange)
                .with(routingKey);
    }
}
