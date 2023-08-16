package com.goorm.profileboxapiadmin.kafka;

import org.springframework.stereotype.Component;

@Component
public class ConsumerService {

    private static final String TOPIC_NAME = "my-topic";

//    @KafkaListener(topics = TOPIC_NAME)
//    public void consumeMessage(String message) {
//        System.out.println("Received message: " + message);
//    }
}

