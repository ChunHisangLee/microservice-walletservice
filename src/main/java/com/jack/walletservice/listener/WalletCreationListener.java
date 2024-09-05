package com.jack.walletservice.listener;

import com.jack.walletservice.entity.Wallet;
import com.jack.walletservice.message.WalletCreationMessage;
import com.jack.walletservice.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WalletCreationListener {

    private static final Logger logger = LoggerFactory.getLogger(WalletCreationListener.class);

    private final WalletService walletService;

    public WalletCreationListener(WalletService walletService) {
        this.walletService = walletService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.walletCreationQueue}")
    public void handleWalletCreationMessage(WalletCreationMessage message) {
        logger.info("Received wallet creation message for user ID: {}", message.getUserId());

        try {
            // Create wallet for the user
            Wallet wallet = walletService.createWallet(message.getUserId());
            walletService.creditWallet(message.getUserId(), message.getInitialBalance());
            logger.info("Wallet created for user ID: {} with initial balance: {}", message.getUserId(), message.getInitialBalance());
        } catch (Exception e) {
            logger.error("Failed to process wallet creation message: {}", e.getMessage());
        }
    }
}
