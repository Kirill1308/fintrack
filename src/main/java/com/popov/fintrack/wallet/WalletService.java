package com.popov.fintrack.wallet;

import com.popov.fintrack.wallet.model.Wallet;

import java.util.List;

public interface WalletService {

    Wallet getWalletById(Long walletId);

    List<Wallet> getWallets(Long userId);

    boolean isOwnerOfWallet(Long userId, Long walletId);

    Wallet updateWallet(Wallet wallet);

    void deleteWallet(Long id);
}
