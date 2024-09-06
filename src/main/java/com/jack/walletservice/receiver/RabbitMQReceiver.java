package com.jack.walletservice.receiver;

import com.jack.walletservice.config.RabbitMQConfig;
import com.jack.walletservice.entity.Wallet;
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
        // Call the method to create a wallet for the user
        createWalletForUser(walletCreationMessage.getUserId(), walletCreationMessage.getInitialBalance());
    }

    private void createWalletForUser(Long userId, Double initialBalance) {
        logger.info("Creating wallet for user: {}", userId);

        // Simulate wallet creation logic
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setUsdBalance(initialBalance);
        wallet.setBtcBalance(0.0);

        // Save wallet to the database using WalletService
        walletService.createWallet(userId);

        logger.info("Wallet created successfully for user: {}", userId);
    }
}
