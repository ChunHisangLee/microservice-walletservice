package com.jack.walletservice.controller;

import com.jack.walletservice.entity.Wallet;
import com.jack.walletservice.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<Wallet> createWallet(@PathVariable Long userId) {
        Wallet wallet = walletService.createWallet(userId);
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<Wallet> getWalletBalance(@PathVariable Long userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(wallet);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateWallet(@PathVariable Long userId, @RequestParam Double usdAmount, @RequestParam Double btcAmount) {
        walletService.updateWallet(userId, usdAmount, btcAmount);
        return ResponseEntity.ok("Wallet updated successfully");
    }
}
