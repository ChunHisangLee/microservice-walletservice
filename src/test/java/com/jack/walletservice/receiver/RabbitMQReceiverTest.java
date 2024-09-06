package com.jack.walletservice.receiver;

import com.jack.walletservice.message.WalletCreationMessage;
import com.jack.walletservice.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


class RabbitMQReceiverTest {

    @Mock
    private WalletService walletService;

    @Mock
    private Logger logger; // You can mock the logger to check if it logs any errors

    @InjectMocks
    private RabbitMQReceiver rabbitMQReceiver;

    @BeforeEach
    void setUp() {
        // Initialize all the mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReceiveMessage_Success() {
        // Arrange
        WalletCreationMessage walletCreationMessage = new WalletCreationMessage(1L, 1000.0);

        // Act
        rabbitMQReceiver.receiveMessage(walletCreationMessage);

        // Assert
        // Verify that walletService.createWallet is called with the correct userId
        verify(walletService, times(1)).createWallet(eq(walletCreationMessage.getUserId()));
        // Verify that walletService.creditWallet is called with the correct userId and initial balance
        verify(walletService, times(1)).creditWallet(eq(walletCreationMessage.getUserId()), eq(walletCreationMessage.getInitialBalance()));
        // Ensure no errors are logged
        verify(logger, never()).error(anyString(), anyLong(), anyString(), any(Throwable.class));
    }

    @Test
    void testReceiveMessage_FailureOnCreateWallet() {
        // Arrange
        WalletCreationMessage walletCreationMessage = new WalletCreationMessage(1L, 1000.0);

        // Simulate a failure in createWallet by throwing an exception
        doThrow(new RuntimeException("Failed to create wallet")).when(walletService).createWallet(walletCreationMessage.getUserId());

        // Act
        rabbitMQReceiver.receiveMessage(walletCreationMessage);

        // Assert
        // Verify that walletService.createWallet is called once
        verify(walletService, times(1)).createWallet(eq(walletCreationMessage.getUserId()));
        // Verify that walletService.creditWallet is never called because createWallet failed
        verify(walletService, never()).creditWallet(anyLong(), anyDouble());
    }
}
