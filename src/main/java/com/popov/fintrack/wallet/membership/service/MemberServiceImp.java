package com.popov.fintrack.wallet.membership.service;

import com.popov.fintrack.user.model.User;
import com.popov.fintrack.wallet.membership.model.Member;
import com.popov.fintrack.wallet.membership.MemberRepository;
import com.popov.fintrack.wallet.membership.MemberService;
import com.popov.fintrack.wallet.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberServiceImp implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Member> findAllMembers(Long walletId) {
        return memberRepository.findByWalletId(walletId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Wallet> findSharedWallets(Long userId) {
        return memberRepository.findSharedWallets(userId);
    }

    @Override
    @Transactional
    public void addMember(Wallet wallet, User recipient) {
        Member member = new Member();
        member.setWallet(wallet);
        member.setUser(recipient);
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void excludeMember(Wallet wallet, User recipient) {
        memberRepository.deleteByWalletIdAndUserId(wallet.getId(), recipient.getId());
    }

    @Override
    @Transactional
    public void deleteAllMembers(Set<Member> members) {
        memberRepository.deleteAll(members);
    }
}
