package com.popov.fintrack.wallet.membership;

import com.popov.fintrack.user.model.User;
import com.popov.fintrack.wallet.membership.model.Member;
import com.popov.fintrack.wallet.model.Wallet;

import java.util.List;
import java.util.Set;

public interface MemberService {

    List<Member> findAllMembers(Long walletId);

    List<Wallet> findSharedWallets(Long userId);

    void addMember(Wallet wallet, User recipient);

    void excludeMember(Wallet wallet, User recipient);

    void deleteAllMembers(Set<Member> members);
}
