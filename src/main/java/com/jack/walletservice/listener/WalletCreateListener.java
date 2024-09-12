package com.jack.walletservice.listener;

import com.jack.walletservice.entity.Wallet;
import com.jack.walletservice.message.WalletCreationMessage;
import com.jack.walletservice.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WalletCreateListener {

    private static final Logger logger = LoggerFactory.getLogger(WalletCreateListener.class);
    private final WalletService walletService;

    public WalletCreateListener(WalletService walletService) {
        this.walletService = walletService;
    }

    @RabbitListener(queues = "${app.wallet.queue.create}")
    public void handleWalletCreation(WalletCreationMessage message) {
        logger.info("Received Wallet Creation message: UserID: {}", message.getUserId());

        try {
            Wallet wallet = walletService.createWallet(message.getUserId());
            walletService.creditWallet(message.getUserId(), message.getInitialBalance());
            logger.info("Wallet created and credited for user ID: {}", message.getUserId());
        } catch (Exception e) {
            logger.error("Failed to process wallet creation for user ID: {}. Error: {}", message.getUserId(), e.getMessage(), e);
        }
    }
}
