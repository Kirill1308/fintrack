package com.popov.fintrack.wallet;

import com.popov.fintrack.user.model.member.Member;
import com.popov.fintrack.wallet.model.Wallet;

import java.util.List;

public interface WalletService {

    Wallet getWalletById(Long walletId);

    List<Wallet> getWallets(Long userId);

    List<Member> findAllMembers(Long walletId);

    boolean isMemberOfWallet(Long userId, Long walletId);

    Wallet createWallet(Wallet wallet);

    Wallet updateWallet(Wallet wallet);

    void deleteWallet(Long id);

    boolean isOwnerOfWallet(Long userId, Long walletId);
}
