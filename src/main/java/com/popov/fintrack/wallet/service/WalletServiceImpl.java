package com.popov.fintrack.wallet.service;

import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.user.service.UserServiceImpl;
import com.popov.fintrack.wallet.WalletRepository;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.model.Wallet;
import com.popov.fintrack.user.MemberRepository;
import com.popov.fintrack.user.MemberService;
import com.popov.fintrack.user.model.member.Member;
import com.popov.fintrack.user.model.member.MemberRole;
import com.popov.fintrack.web.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final UserServiceImpl userService;

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
    public List<Member> findAllMembers(Long walletId) {
        return memberService.findAllMembers(walletId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isMemberOfWallet(Long userId, Long walletId) {
        return walletRepository.existsByUserIdAndId(userId, walletId);
    }

    @Override
    @Transactional
    public Wallet createWallet(Wallet wallet) {
        Wallet savedWallet = walletRepository.save(wallet);
        Long userId = SecurityUtils.getAuthenticatedUserId();

        Member member = new Member();
        member.setUser(userService.getUserById(userId));
        member.setWallet(savedWallet);
        member.setRole(MemberRole.OWNER);
        memberRepository.save(member);

        return savedWallet;
    }

    @Override
    @Transactional
    public Wallet updateWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public void deleteWallet(Long id) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        memberRepository.deleteAll(wallet.getMembers());

        walletRepository.delete(wallet);
    }
}
