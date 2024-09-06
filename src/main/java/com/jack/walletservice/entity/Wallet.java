package com.jack.walletservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "wallet", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id")
})
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User ID cannot be null")
    @Column(name = "user_id", nullable = false, updatable = false) // Making user ID non-updatable
    private Long userId;

    @Min(value = 0, message = "USD balance must be non-negative")
    @Column(name = "usd_balance", nullable = false, columnDefinition = "DECIMAL(18, 2) DEFAULT 0.0") // Added precision for financial data
    private Double usdBalance;

    @Min(value = 0, message = "BTC balance must be non-negative")
    @Column(name = "btc_balance", nullable = false, columnDefinition = "DECIMAL(18, 8) DEFAULT 0.0") // Increased precision for BTC
    private Double btcBalance;
}
