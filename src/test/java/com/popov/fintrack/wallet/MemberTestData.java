package com.popov.fintrack.wallet;

import com.popov.fintrack.wallet.membership.model.Member;

import static com.popov.fintrack.user.UserTestData.user;
import static com.popov.fintrack.wallet.WalletTestData.wallet;

public class MemberTestData {

    public static final long MEMBER_ID = 2L;
    public static final Member member;

    static {

        member = new Member();
        member.setId(MEMBER_ID);
        member.setWallet(wallet);
        member.setUser(user);
    }
}
