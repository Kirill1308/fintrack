package com.popov.fintrack.wallet;

import com.popov.fintrack.MatcherFactory;
import com.popov.fintrack.wallet.dto.WalletDTO;
import com.popov.fintrack.wallet.model.Wallet;

import static com.popov.fintrack.user.UserTestData.user;

public class WalletTestData {

    public static final MatcherFactory.Matcher<WalletDTO> WALLET_DTO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(WalletDTO.class);

    public static final long WALLET_ID = 2;
    public static final long SHARED_WALLET_ID = 2;
    public static final long USER_1_WALLET_ID = 2;

    public static final Wallet wallet;
    public static final WalletDTO walletDTO;
    public static final Wallet sharedWallet;

    static {
        wallet = new Wallet();
        wallet.setId(WALLET_ID);
        wallet.setOwner(user);
        wallet.setName("User Wallet");
        wallet.setBalance(2000.0);
        wallet.setCurrency("USD");

        walletDTO = new WalletDTO();
        walletDTO.setId(WALLET_ID);
        walletDTO.setName("User Wallet");
        walletDTO.setBalance(2000.0);
        walletDTO.setCurrency("USD");

        sharedWallet = new Wallet();
        sharedWallet.setId(SHARED_WALLET_ID);
        sharedWallet.setOwner(user);
    }
}
