package com.jack.walletservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue walletQueue() {
        return new Queue("walletCreationQueue", false);
    }
}
