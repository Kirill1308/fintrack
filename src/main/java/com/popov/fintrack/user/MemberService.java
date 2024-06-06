package com.popov.fintrack.user;

import com.popov.fintrack.user.model.User;
import com.popov.fintrack.user.model.member.Member;
import com.popov.fintrack.wallet.model.Wallet;

import java.util.List;

public interface MemberService {

    List<Member> findAllMembers(Long walletId);

    void addMember(Wallet wallet, User recipient);

    void excludeMember(Wallet wallet, User recipient);
}
