package com.eyun.commission.web.rest;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.eyun.commission.domain.Greeting;
import com.eyun.commission.kafka.ProducerChannel;

@RestController
@RequestMapping("/api")
public class ProducerResource {
    private MessageChannel channel;

    public ProducerResource(ProducerChannel channel) {
        this.channel = channel.messageChannel();
    }

    @GetMapping("/greetings/{messages}")
    @Timed
    public void produce(@PathVariable String messages) {

        System.out.println("收到消息"+messages);
            channel.send(MessageBuilder.withPayload(messages).build());



    }
}
