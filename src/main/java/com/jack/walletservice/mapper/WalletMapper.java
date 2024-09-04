package com.jack.walletservice.mapper;

import com.jack.walletservice.dto.WalletDTO;
import com.jack.walletservice.entity.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    /**
     * Converts a Wallet entity to a WalletDTO.
     *
     * @param wallet the Wallet entity
     * @return the corresponding WalletDTO
     */
    public WalletDTO toDto(Wallet wallet) {
        if (wallet == null) {
            return null;
        }
        return WalletDTO.builder()
                .id(wallet.getId())
                .usdBalance(wallet.getUsdBalance())
                .btcBalance(wallet.getBtcBalance())
                .build();
    }

    /**
     * Converts a WalletDTO to a Wallet entity.
     *
     * @param walletDTO the WalletDTO
     * @return the corresponding Wallet entity
     */
    public Wallet toEntity(WalletDTO walletDTO) {
        if (walletDTO == null) {
            return null;
        }
        return Wallet.builder()
                .usdBalance(walletDTO.getUsdBalance())
                .btcBalance(walletDTO.getBtcBalance())
                .build();
    }
}
