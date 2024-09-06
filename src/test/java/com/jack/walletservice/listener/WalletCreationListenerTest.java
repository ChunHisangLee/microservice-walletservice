package com.jack.walletservice.listener;

import com.jack.walletservice.entity.Wallet;
import com.jack.walletservice.message.WalletCreationMessage;
import com.jack.walletservice.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class WalletCreationListenerTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletCreationListener walletCreationListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleWalletCreationMessage_Success() {
        // Arrange
        WalletCreationMessage message = new WalletCreationMessage(1L, 1000.0);
        Wallet mockWallet = mock(Wallet.class);
        when(walletService.createWallet(anyLong())).thenReturn(mockWallet);

        // Act
        walletCreationListener.handleWalletCreationMessage(message);

        // Assert
        verify(walletService, times(1)).createWallet(1L);
        verify(walletService, times(1)).creditWallet(1L, 1000.0);
        verifyNoMoreInteractions(walletService);
    }

    @Test
    void testHandleWalletCreationMessage_Failure() {
        // Arrange
        WalletCreationMessage message = new WalletCreationMessage(1L, 1000.0);
        when(walletService.createWallet(anyLong())).thenThrow(new RuntimeException("Wallet creation failed"));

        // Act
        walletCreationListener.handleWalletCreationMessage(message);

        // Assert
        verify(walletService, times(1)).createWallet(1L);
        verify(walletService, never()).creditWallet(anyLong(), anyDouble());
    }
}
