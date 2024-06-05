package com.popov.fintrack.wallet.service;

import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.user.UserRepository;
import com.popov.fintrack.wallet.WalletRepository;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.model.Wallet;
import com.popov.fintrack.web.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Wallet getWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Wallet> getWallets(Long userId) {
        return walletRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOwnerOfWallet(Long userId, Long walletId) {
        return walletRepository.existsByUserIdAndId(userId, walletId);
    }

    @Override
    @Transactional
    public Wallet updateWallet(Wallet wallet) {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        wallet.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public void deleteWallet(Long id) {
        walletRepository.deleteById(id);
    }
}
