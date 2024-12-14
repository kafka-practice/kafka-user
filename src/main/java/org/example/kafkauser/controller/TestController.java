package org.example.kafkauser.controller;

import lombok.RequiredArgsConstructor;
import org.example.kafkauser.service.test.TestKafkaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final TestKafkaService testKafkaService;

    @GetMapping("kafka")
    public ResponseEntity<String> sendMessageToKafka() {
        testKafkaService.sendToKafka();
        return ResponseEntity.ok("Successfully sent message");
    }
}
