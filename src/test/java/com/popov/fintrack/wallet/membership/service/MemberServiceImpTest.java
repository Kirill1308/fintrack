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

import static com.popov.fintrack.user.UserTestData.USER1_ID;
import static com.popov.fintrack.user.UserTestData.user;
import static com.popov.fintrack.user.UserTestData.walletMember;
import static com.popov.fintrack.wallet.MemberTestData.member;
import static com.popov.fintrack.wallet.WalletTestData.WALLET_ID;
import static com.popov.fintrack.wallet.WalletTestData.sharedWallet;
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
        when(memberRepository.findByWalletId(WALLET_ID)).thenReturn(List.of(member));

        List<Member> members = memberService.findAllMembers(WALLET_ID);

        assertNotNull(members);
        assertEquals(1, members.size());
        verify(memberRepository, times(1)).findByWalletId(WALLET_ID);
    }

    @Test
    void findSharedWallets_success() {
        when(memberRepository.findSharedWallets(USER1_ID)).thenReturn(List.of(sharedWallet));

        List<Wallet> wallets = memberService.findSharedWallets(USER1_ID);

        assertNotNull(wallets);
        assertEquals(1, wallets.size());
        verify(memberRepository, times(1)).findSharedWallets(USER1_ID);
    }

    @Test
    void addMember_success() {
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        memberService.addMember(wallet, user);

        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void excludeMember_success() {
        memberService.excludeMember(wallet, walletMember);
        verify(memberRepository, times(1))
                .deleteByWalletIdAndUserId(WALLET_ID, walletMember.getId());
    }

    @Test
    void deleteAllMembers_success() {
        Set<Member> members = Collections.singleton(member);
        doNothing().when(memberRepository).deleteAll(members);

        memberService.deleteAllMembers(members);

        verify(memberRepository, times(1)).deleteAll(members);
    }
}
