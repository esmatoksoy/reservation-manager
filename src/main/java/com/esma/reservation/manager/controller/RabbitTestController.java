package com.esma.reservation.manager.controller;

import com.esma.reservation.manager.service.RabbitPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class RabbitTestController {

    private final RabbitPublisher publisher;

    @PostMapping("/publish-10")
    public ResponseEntity<String> publishTenMessages() {
        for (int i = 1; i <= 10; i++) {
            publisher.send("x"+i);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Published 10 test messages");
    }
}

