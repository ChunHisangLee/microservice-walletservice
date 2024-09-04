package com.jack.walletservice.service.impl;

import com.jack.walletservice.entity.Wallet;
import com.jack.walletservice.exception.WalletNotFoundException;
import com.jack.walletservice.message.WalletCreationMessage;
import com.jack.walletservice.repository.WalletRepository;
import com.jack.walletservice.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletServiceImpl implements WalletService {
    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    private final WalletRepository walletRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Wallet createWallet(Long userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setUsdBalance(1000.0); // Initial USD balance
        wallet.setBtcBalance(0.0); // Initial BTC balance
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user ID: " + userId));
    }

    @Transactional
    @Override
    public void updateWallet(Long userId, Double usdAmount, Double btcAmount) {
        Wallet wallet = getWalletByUserId(userId);
        wallet.setUsdBalance(wallet.getUsdBalance() + usdAmount);
        wallet.setBtcBalance(wallet.getBtcBalance() + btcAmount);
        walletRepository.save(wallet);
    }

    @Transactional
    @Override
    public void creditWallet(Long userId, Double amount) {
        Wallet wallet = getWalletByUserId(userId);
        wallet.setUsdBalance(wallet.getUsdBalance() + amount);
        walletRepository.save(wallet);
    }

    @Transactional
    @Override
    public void debitWallet(Long userId, Double amount) {
        Wallet wallet = getWalletByUserId(userId);
        if (wallet.getUsdBalance() < amount) {
            throw new IllegalArgumentException("Insufficient USD balance.");
        }
        wallet.setUsdBalance(wallet.getUsdBalance() - amount);
        walletRepository.save(wallet);
    }

    @RabbitListener(queues = "walletCreationQueue")
    public void handleWalletCreation(WalletCreationMessage message) {
        logger.info("Received wallet creation message for user ID: {}", message.getUserId());

        // Create a new wallet
        Wallet wallet = new Wallet();
        wallet.setUsdBalance(message.getInitialBalance());
        wallet.setBtcBalance(0.0);

        // Associate the wallet with the user ID and save it
        walletRepository.save(wallet);

        logger.info("Wallet created for user ID: {} with initial balance: {}", message.getUserId(), message.getInitialBalance());
    }
}
