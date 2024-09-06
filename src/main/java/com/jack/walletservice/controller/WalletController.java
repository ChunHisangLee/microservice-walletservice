package com.jack.walletservice.controller;

import com.jack.walletservice.entity.Wallet;
import com.jack.walletservice.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    // Create a new wallet for a given user
    @PostMapping("/create/{userId}")
    public ResponseEntity<Wallet> createWallet(@PathVariable Long userId) {
        Wallet wallet = walletService.createWallet(userId);
        return ResponseEntity.ok(wallet);  // Return the created wallet
    }

    // Retrieve the wallet balance for a given user
    @GetMapping("/balance/{userId}")
    public ResponseEntity<Wallet> getWalletBalance(@PathVariable Long userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(wallet);  // Return the wallet containing balance info
    }

    // Update wallet's USD and BTC balances
    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateWallet(
            @PathVariable Long userId,
            @RequestParam Double usdAmount,
            @RequestParam Double btcAmount) {

        walletService.updateWallet(userId, usdAmount, btcAmount);
        return ResponseEntity.ok("Wallet for user ID " + userId + " updated successfully");
    }
}
