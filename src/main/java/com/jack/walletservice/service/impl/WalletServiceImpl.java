package com.jack.walletservice.service.impl;

import com.jack.walletservice.entity.Wallet;
import com.jack.walletservice.exception.WalletNotFoundException;
import com.jack.walletservice.repository.WalletRepository;
import com.jack.walletservice.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

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
}
