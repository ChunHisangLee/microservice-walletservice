package com.jack.walletservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue walletCreationQueue() {
        // Define the queue for wallet creation messages
        return new Queue("walletCreationQueue", true);
    }

    @Bean
    public DirectExchange walletExchange() {
        // Define the direct exchange that will route the messages to the queue
        return new DirectExchange("walletExchange");
    }

    @Bean
    public Binding walletQueueBinding(Queue walletCreationQueue, DirectExchange walletExchange) {
        // Bind the queue to the exchange with the specific routing key
        return BindingBuilder.bind(walletCreationQueue).to(walletExchange).with("walletRoutingKey");
    }
}
