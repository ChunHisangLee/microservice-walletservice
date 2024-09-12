package com.jack.walletservice.listener;

import com.jack.walletservice.entity.Wallet;
import com.jack.walletservice.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WalletBalanceListener {

    private static final Logger logger = LoggerFactory.getLogger(WalletBalanceListener.class);
    private final WalletService walletService;

    public WalletBalanceListener(WalletService walletService) {
        this.walletService = walletService;
    }

    @RabbitListener(queues = "${app.wallet.queue.balance}")
    public Wallet handleWalletBalanceRequest(Long userId) {
        logger.info("Received Wallet Balance Request for UserID: {}", userId);

        try {
            Wallet wallet = walletService.getWalletByUserId(userId);
            return wallet;
        } catch (Exception e) {
            logger.error("Failed to retrieve wallet balance for user ID: {}. Error: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve wallet balance.");
        }
    }
}
