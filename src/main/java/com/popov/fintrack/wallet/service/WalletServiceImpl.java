package com.popov.fintrack.wallet.service;

import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.user.UserService;
import com.popov.fintrack.wallet.WalletRepository;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.membership.MemberService;
import com.popov.fintrack.wallet.membership.model.Member;
import com.popov.fintrack.wallet.model.Wallet;
import com.popov.fintrack.web.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final UserService userService;
    private final MemberService memberService;

    private final WalletRepository walletRepository;

    @Override
    @Transactional(readOnly = true)
    public Wallet getWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Wallet> getWallets(Long userId) {
        List<Wallet> ownedWallets = walletRepository.findOwnedWallets(userId);
        List<Wallet> sharedWallets = memberService.findSharedWallets(userId);

        List<Wallet> wallets = new ArrayList<>();
        wallets.addAll(ownedWallets);
        wallets.addAll(sharedWallets);

        return wallets;
    }

    @Override
    public List<Member> findAllMembers(Long walletId) {
        return memberService.findAllMembers(walletId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Wallet> getMemberWallets(Long userId) {
        return walletRepository.findByMembersUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOwnerOfWallet(Long userId, Long walletId) {
        log.debug("Checking if user with ID: {} is an owner of wallet with ID: {}", userId, walletId);
        return walletRepository.existsByOwner_IdAndId(userId, walletId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isMemberOfWallet(Long userId, Long walletId) {
        log.debug("Checking if user with ID: {} is a member of wallet with ID: {}", userId, walletId);
        return walletRepository.existsByUserIdAndId(userId, walletId);
    }

    @Override
    @Transactional
    public Wallet createWallet(Wallet wallet) {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        wallet.setOwner(userService.getUserById(userId));
        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public Wallet updateWallet(Wallet wallet) {
        Wallet existingWallet = walletRepository.findById(wallet.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        return walletRepository.save(existingWallet);
    }

    @Override
    @Transactional
    public void deleteWallet(Long id) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        memberService.deleteAllMembers(wallet.getMembers());
        walletRepository.delete(wallet);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Wallet> getWalletsByIds(List<Long> walletIds) {
        return walletRepository.findByIdIn(walletIds);
    }
}
