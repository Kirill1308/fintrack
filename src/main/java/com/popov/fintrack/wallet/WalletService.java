package com.popov.fintrack.wallet;

import com.popov.fintrack.wallet.membership.model.Member;
import com.popov.fintrack.wallet.model.Wallet;

import java.util.List;

public interface WalletService {

    Wallet getWalletById(Long walletId);

    List<Wallet> getWallets(Long userId);

    List<Member> findAllMembers(Long walletId);

    boolean isMemberOfWallet(Long userId, Long walletId);

    boolean isOwnerOfWallet(Long userId, Long walletId);

    Wallet createWallet(Wallet wallet);

    Wallet updateWallet(Wallet wallet);

    void deleteWallet(Long id);

    List<Wallet> getWalletsByIds(List<Long> walletIds);

    List<Wallet> getMemberWallets(Long userId);
}
