package com.jack.walletservice.controller;

import com.jack.walletservice.entity.Wallet;
import com.jack.walletservice.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        when(walletService.createWallet(anyLong())).thenReturn(wallet);

        // Act
        ResponseEntity<Wallet> response = walletController.createWallet(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(wallet, response.getBody());
        verify(walletService, times(1)).createWallet(1L);
    }

    @Test
    void getWalletBalance_Success() {
        // Arrange
        when(walletService.getWalletByUserId(anyLong())).thenReturn(wallet);

        // Act
        ResponseEntity<Wallet> response = walletController.getWalletBalance(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(wallet, response.getBody());
        verify(walletService, times(1)).getWalletByUserId(1L);
    }

    @Test
    void updateWallet_Success() {
        // Arrange
        doNothing().when(walletService).updateWallet(anyLong(), anyDouble(), anyDouble());

        // Act
        ResponseEntity<String> response = walletController.updateWallet(1L, 200.0, 0.1);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Wallet updated successfully", response.getBody());
        verify(walletService, times(1)).updateWallet(1L, 200.0, 0.1);
    }
}
