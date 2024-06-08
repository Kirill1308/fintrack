package com.popov.fintrack.user.service;

import com.popov.fintrack.user.MemberRepository;
import com.popov.fintrack.user.MemberService;
import com.popov.fintrack.user.model.User;
import com.popov.fintrack.user.model.member.Member;
import com.popov.fintrack.wallet.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
