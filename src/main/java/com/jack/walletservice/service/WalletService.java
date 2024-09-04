package com.jack.walletservice.service;

import com.jack.walletservice.entity.Wallet;

public interface WalletService {
    Wallet createWallet(Long userId);
    Wallet getWalletByUserId(Long userId);
    void updateWallet(Long userId, Double usdAmount, Double btcAmount);
    void creditWallet(Long userId, Double amount);
    void debitWallet(Long userId, Double amount);
}
