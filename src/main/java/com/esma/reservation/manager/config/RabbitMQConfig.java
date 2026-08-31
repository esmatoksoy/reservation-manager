package com.esma.reservation.manager.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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
    private String routingKey;

    // Dead Letter Queue configuration
    private static final String DLQ_SUFFIX = ".dlq";
    private static final String DLQ_EXCHANGE_SUFFIX = ".dlx";

    @Bean
    public Queue reservationMailQueue() {
        // Attach dead-letter exchange so failed messages are routed to the DLQ
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", exchangeName + DLQ_EXCHANGE_SUFFIX)
                .withArgument("x-dead-letter-routing-key", queueName + DLQ_SUFFIX)
                .build();
    }

    @Bean
    public Queue reservationMailDlq() {
        return QueueBuilder.durable(queueName + DLQ_SUFFIX).build();
    }

    @Bean
    public DirectExchange reservationExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public DirectExchange reservationDeadLetterExchange() {
        return new DirectExchange(exchangeName + DLQ_EXCHANGE_SUFFIX);
    }

    @Bean
    public Binding reservationMailBinding(Queue reservationMailQueue, DirectExchange reservationExchange) {
        return BindingBuilder.bind(reservationMailQueue)
                .to(reservationExchange)
                .with(routingKey);
    }

    @Bean
    public Binding reservationMailDlqBinding(Queue reservationMailDlq, DirectExchange reservationDeadLetterExchange) {
        return BindingBuilder.bind(reservationMailDlq)
                .to(reservationDeadLetterExchange)
                .with(queueName + DLQ_SUFFIX);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
