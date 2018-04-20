package com.eyun.commission.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import com.eyun.commission.domain.Greeting;
import com.eyun.commission.kafka.ConsumerChannel;

@Service
public class ConsumerService {
    private final Logger log = LoggerFactory.getLogger(ConsumerService.class);


    @StreamListener(ConsumerChannel.CHANNEL)
    public void consume(String greeting) {
        log.info("Received message: {}.", greeting);
    }
}
