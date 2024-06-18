package com.popov.fintrack.wallet.membership.service;

import com.popov.fintrack.wallet.membership.MemberRepository;
import com.popov.fintrack.wallet.membership.model.Member;
import com.popov.fintrack.wallet.model.Wallet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.popov.fintrack.user.UserTestData.user;
import static com.popov.fintrack.user.UserTestData.userMember;
import static com.popov.fintrack.wallet.MemberTestData.member;
import static com.popov.fintrack.wallet.WalletTestData.WALLET_ID;
import static com.popov.fintrack.wallet.WalletTestData.wallet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceImpTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImp memberService;

    @Test
    void findAllMembers_success() {
        when(memberRepository.findByWalletId(1L)).thenReturn(List.of(member));

        List<Member> members = memberService.findAllMembers(WALLET_ID);

        assertNotNull(members);
        assertEquals(1, members.size());
        verify(memberRepository, times(1)).findByWalletId(WALLET_ID);
    }

    @Test
    void findSharedWallets_success() {
        when(memberRepository.findSharedWallets(1L)).thenReturn(List.of(wallet));

        List<Wallet> wallets = memberService.findSharedWallets(1L);

        assertNotNull(wallets);
        assertEquals(1, wallets.size());
        verify(memberRepository, times(1)).findSharedWallets(1L);
    }

    @Test
    void addMember_success() {
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        memberService.addMember(wallet, user);

        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void excludeMember_success() {
        memberService.excludeMember(wallet, userMember);
        verify(memberRepository, times(1)).deleteByWalletIdAndUserId(WALLET_ID, 2L);
    }

    @Test
    void deleteAllMembers_success() {
        Set<Member> members = Collections.singleton(member);
        doNothing().when(memberRepository).deleteAll(members);

        memberService.deleteAllMembers(members);

        verify(memberRepository, times(1)).deleteAll(members);
    }
}
