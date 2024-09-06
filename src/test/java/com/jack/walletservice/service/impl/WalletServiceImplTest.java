package com.jack.walletservice.service.impl;

import com.jack.walletservice.entity.Wallet;
import com.jack.walletservice.exception.WalletNotFoundException;
import com.jack.walletservice.message.WalletCreationMessage;
import com.jack.walletservice.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    private Wallet wallet;

    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImplTest.class);

    @BeforeEach
    void setUp() {
        wallet = Wallet.builder()
                .id(1L)
                .userId(1L)
                .usdBalance(1000.0)
                .btcBalance(0.5)
                .build();
    }

    @Test
    void createWallet_Success() {
        // Arrange
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        // Act
        Wallet createdWallet = walletService.createWallet(1L);

        // Assert
        assertNotNull(createdWallet);
        assertEquals(wallet.getUserId(), createdWallet.getUserId());
        assertEquals(wallet.getUsdBalance(), createdWallet.getUsdBalance());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void getWalletByUserId_Success() {
        // Arrange
        when(walletRepository.findByUserId(anyLong())).thenReturn(Optional.of(wallet));

        // Act
        Wallet foundWallet = walletService.getWalletByUserId(1L);

        // Assert
        assertNotNull(foundWallet);
        assertEquals(wallet.getUserId(), foundWallet.getUserId());
        assertEquals(wallet.getUsdBalance(), foundWallet.getUsdBalance());
        verify(walletRepository, times(1)).findByUserId(anyLong());
    }

    @Test
    void getWalletByUserId_WalletNotFound() {
        // Arrange
        when(walletRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        WalletNotFoundException exception = assertThrows(WalletNotFoundException.class, () -> walletService.getWalletByUserId(1L));
        assertEquals("Wallet not found for user ID: 1", exception.getMessage());
        verify(walletRepository, times(1)).findByUserId(anyLong());
    }

    @Test
    void updateWallet_Success() {
        // Arrange
        when(walletRepository.findByUserId(anyLong())).thenReturn(Optional.of(wallet));

        // Act
        walletService.updateWallet(1L, 200.0, 0.1);

        // Assert
        assertEquals(1200.0, wallet.getUsdBalance());
        assertEquals(0.6, wallet.getBtcBalance());
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void creditWallet_Success() {
        // Arrange
        when(walletRepository.findByUserId(anyLong())).thenReturn(Optional.of(wallet));

        // Act
        walletService.creditWallet(1L, 200.0);

        // Assert
        assertEquals(1200.0, wallet.getUsdBalance());
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void debitWallet_Success() {
        // Arrange
        when(walletRepository.findByUserId(anyLong())).thenReturn(Optional.of(wallet));

        // Act
        walletService.debitWallet(1L, 200.0);

        // Assert
        assertEquals(800.0, wallet.getUsdBalance());
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void debitWallet_InsufficientBalance() {
        // Arrange
        when(walletRepository.findByUserId(anyLong())).thenReturn(Optional.of(wallet));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> walletService.debitWallet(1L, 1200.0));
        assertEquals("Insufficient USD balance.", exception.getMessage());
        verify(walletRepository, never()).save(wallet);
    }
}
