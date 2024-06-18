package com.popov.fintrack.wallet;

import com.popov.fintrack.MatcherFactory;
import com.popov.fintrack.wallet.dto.WalletDTO;
import com.popov.fintrack.wallet.model.Wallet;

public class WalletTestData {

    public static final MatcherFactory.Matcher<WalletDTO> WALLET_DTO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(WalletDTO.class);

    public static final long WALLET_ID = 1;
    public static final Wallet wallet;
    public static final WalletDTO walletDTO;

    static {
        wallet = new Wallet();
        wallet.setId(WALLET_ID);
        wallet.setName("Test Wallet");

        walletDTO = new WalletDTO();
        walletDTO.setId(WALLET_ID);
        walletDTO.setName("Test Wallet");
        walletDTO.setCurrency("USD");
    }
}
