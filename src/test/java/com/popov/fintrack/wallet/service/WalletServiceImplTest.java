package com.popov.fintrack.wallet.service;

import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.user.UserService;
import com.popov.fintrack.user.model.User;
import com.popov.fintrack.wallet.WalletRepository;
import com.popov.fintrack.wallet.membership.MemberService;
import com.popov.fintrack.wallet.membership.model.Member;
import com.popov.fintrack.wallet.model.Wallet;
import com.popov.fintrack.web.security.utils.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private MemberService memberService;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    private Wallet wallet;
    private Member member;
    private List<Wallet> walletList;

    @BeforeEach
    void setUp() {
        wallet = new Wallet();
        wallet.setId(1L);
        wallet.setMembers(new HashSet<>());

        User owner = new User();
        owner.setId(1L);

        member = new Member();
        member.setUser(owner);
        member.setWallet(wallet);

        walletList = new ArrayList<>();
        walletList.add(wallet);
    }

    @Test
    void getWalletById_success() {
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));

        Wallet foundWallet = walletService.getWalletById(1L);

        assertNotNull(foundWallet);
        assertEquals(1L, foundWallet.getId());
        verify(walletRepository, times(1)).findById(1L);
    }

    @Test
    void getWalletById_notFound() {
        when(walletRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> walletService.getWalletById(1L));
        verify(walletRepository, times(1)).findById(1L);
    }

    @Test
    void getWallets_success() {
        when(walletRepository.findOwnedWallets(1L)).thenReturn(walletList);
        when(memberService.findSharedWallets(1L)).thenReturn(walletList);

        List<Wallet> wallets = walletService.getWallets(1L);

        assertNotNull(wallets);
        assertEquals(2, wallets.size());
        verify(walletRepository, times(1)).findOwnedWallets(1L);
        verify(memberService, times(1)).findSharedWallets(1L);
    }

    @Test
    void findAllMembers_success() {
        when(memberService.findAllMembers(1L)).thenReturn(List.of(member));

        List<Member> members = walletService.findAllMembers(1L);

        assertNotNull(members);
        assertEquals(1, members.size());
        verify(memberService, times(1)).findAllMembers(1L);
    }

    @Test
    void getMemberWallets_success() {
        when(walletRepository.findByMembersUserId(1L)).thenReturn(walletList);

        List<Wallet> wallets = walletService.getMemberWallets(1L);

        assertNotNull(wallets);
        assertEquals(1, wallets.size());
        verify(walletRepository, times(1)).findByMembersUserId(1L);
    }

    @Test
    void isOwnerOfWallet_true() {
        when(walletRepository.existsByOwner_IdAndId(1L, 1L)).thenReturn(true);

        boolean isOwner = walletService.isOwnerOfWallet(1L, 1L);

        assertTrue(isOwner);
        verify(walletRepository, times(1)).existsByOwner_IdAndId(1L, 1L);
    }

    @Test
    void isOwnerOfWallet_false() {
        when(walletRepository.existsByOwner_IdAndId(1L, 1L)).thenReturn(false);

        boolean isOwner = walletService.isOwnerOfWallet(1L, 1L);

        assertFalse(isOwner);
        verify(walletRepository, times(1)).existsByOwner_IdAndId(1L, 1L);
    }

    @Test
    void isMemberOfWallet_true() {
        when(walletRepository.existsByUserIdAndId(1L, 1L)).thenReturn(true);

        boolean isMember = walletService.isMemberOfWallet(1L, 1L);

        assertTrue(isMember);
        verify(walletRepository, times(1)).existsByUserIdAndId(1L, 1L);
    }

    @Test
    void isMemberOfWallet_false() {
        when(walletRepository.existsByUserIdAndId(1L, 1L)).thenReturn(false);

        boolean isMember = walletService.isMemberOfWallet(1L, 1L);

        assertFalse(isMember);
        verify(walletRepository, times(1)).existsByUserIdAndId(1L, 1L);
    }

    @Test
    void createWallet_success() {
        when(userService.getUserById(anyLong())).thenReturn(wallet.getOwner());
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getAuthenticatedUserId).thenReturn(1L);

            Wallet createdWallet = walletService.createWallet(wallet);

            assertNotNull(createdWallet);
            assertEquals(wallet.getOwner(), createdWallet.getOwner());
            verify(walletRepository, times(1)).save(any(Wallet.class));
        }
    }

    @Test
    void updateWallet_success() {
        when(walletRepository.findById(wallet.getId())).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Wallet updatedWallet = walletService.updateWallet(wallet);

        assertNotNull(updatedWallet);
        assertEquals(1L, updatedWallet.getId());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void deleteWallet_success() {
        wallet.setMembers(Set.of(member));
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        doNothing().when(memberService).deleteAllMembers(anySet());
        doNothing().when(walletRepository).delete(wallet);

        walletService.deleteWallet(1L);

        verify(walletRepository, times(1)).findById(1L);
        verify(memberService, times(1)).deleteAllMembers(anySet());
        verify(walletRepository, times(1)).delete(wallet);
    }

    @Test
    void deleteWallet_notFound() {
        when(walletRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> walletService.deleteWallet(1L));
        verify(walletRepository, times(1)).findById(1L);
    }

    @Test
    void getWalletsByIds_success() {
        when(walletRepository.findByIdIn(anyList())).thenReturn(walletList);

        List<Wallet> wallets = walletService.getWalletsByIds(List.of(1L, 2L));

        assertNotNull(wallets);
        assertEquals(1, wallets.size());
        verify(walletRepository, times(1)).findByIdIn(anyList());
    }
}
