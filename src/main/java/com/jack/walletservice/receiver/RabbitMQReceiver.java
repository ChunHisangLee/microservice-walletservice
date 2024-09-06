package com.jack.walletservice.receiver;

import com.jack.walletservice.config.RabbitMQConfig;
import com.jack.walletservice.message.WalletCreationMessage;
import com.jack.walletservice.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQReceiver {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQReceiver.class);

    private final WalletService walletService;

    public RabbitMQReceiver(WalletService walletService) {
        this.walletService = walletService;
    }

    // Listening to the queue for WalletCreationMessage
    @RabbitListener(queues = RabbitMQConfig.USER_CREATED_QUEUE)
    public void receiveMessage(WalletCreationMessage walletCreationMessage) {
        logger.info("Received User Created Event: <UserID: {}, Initial Balance: {}>", walletCreationMessage.getUserId(), walletCreationMessage.getInitialBalance());

        try {
            // Create a wallet for the user
            walletService.createWallet(walletCreationMessage.getUserId());

            // Credit the initial balance
            walletService.creditWallet(walletCreationMessage.getUserId(), walletCreationMessage.getInitialBalance());
            logger.info("Wallet created and credited with initial balance for user ID: {}", walletCreationMessage.getUserId());
        } catch (Exception e) {
            logger.error("Failed to process wallet creation message for user ID: {}. Error: {}", walletCreationMessage.getUserId(), e.getMessage(), e);
        }
    }
}
