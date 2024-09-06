package com.jack.walletservice.service.impl;

import com.jack.walletservice.entity.Wallet;
import com.jack.walletservice.exception.InsufficientFundsException;
import com.jack.walletservice.exception.WalletNotFoundException;
import com.jack.walletservice.repository.WalletRepository;
import com.jack.walletservice.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Transactional
    @Override
    public Wallet createWallet(Wallet wallet) {
        wallet.setUsdBalance(wallet.getUsdBalance() == null ? 0.0 : wallet.getUsdBalance()); // Default USD balance
        wallet.setBtcBalance(wallet.getBtcBalance() == null ? 0.0 : wallet.getBtcBalance()); // Default BTC balance
        logger.info("Wallet created for user ID: {}", wallet.getUserId());
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet getWalletByUserId(Long userId) {
        logger.info("Fetching wallet for user ID: {}", userId);
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for user ID: " + userId));
    }

    @Transactional
    @Override
    public void updateWallet(Long userId, Double usdAmount, Double btcAmount) {
        Wallet wallet = getWalletByUserId(userId);
        logger.info("Updating wallet for user ID: {} | USD: {} | BTC: {}", userId, usdAmount, btcAmount);
        wallet.setUsdBalance(wallet.getUsdBalance() + usdAmount);
        wallet.setBtcBalance(wallet.getBtcBalance() + btcAmount);
        walletRepository.save(wallet);
        logger.info("Wallet updated successfully for user ID: {}", userId);
    }

    @Transactional
    @Override
    public void creditWallet(Long userId, Double amount) {
        Wallet wallet = getWalletByUserId(userId);
        logger.info("Crediting {} USD to wallet of user ID: {}", amount, userId);
        wallet.setUsdBalance(wallet.getUsdBalance() + amount);
        walletRepository.save(wallet);
        logger.info("Wallet credited successfully for user ID: {}", userId);
    }

    @Transactional
    @Override
    public void debitWallet(Long userId, Double amount) {
        Wallet wallet = getWalletByUserId(userId);
        logger.info("Debiting {} USD from wallet of user ID: {}", amount, userId);

        if (wallet.getUsdBalance() < amount) {
            logger.error("Insufficient balance. Attempted to debit {} USD from user ID: {}", amount, userId);
            throw new InsufficientFundsException("Insufficient USD balance.");
        }

        wallet.setUsdBalance(wallet.getUsdBalance() - amount);
        walletRepository.save(wallet);
        logger.info("Wallet debited successfully for user ID: {}", userId);
    }
}
