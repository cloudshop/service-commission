package com.eyun.commission.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ConsumerChannel {
    public String CHANNEL = "subscribableChannel";

    @Input
    SubscribableChannel subscribableChannel();
}
