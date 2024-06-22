package com.popov.fintrack.transaction;

import com.popov.fintrack.MatcherFactory;
import com.popov.fintrack.transaction.dto.TransactionDTO;
import com.popov.fintrack.transaction.model.Transaction;

import static com.popov.fintrack.user.UserTestData.user;
import static com.popov.fintrack.wallet.WalletTestData.wallet;

public class TransactionTestData {

    public static final MatcherFactory.Matcher<TransactionDTO> TRANSACTION_DTO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(TransactionDTO.class);

    public static final long USER_1_TRANSACTION_ID = 2;

    public static final Transaction transaction;
    public static final TransactionDTO transactionDTO;

    static {
        transaction = new Transaction();
        transaction.setId(USER_1_TRANSACTION_ID);
        transaction.setOwner(user);
        transaction.setWallet(wallet);

        transactionDTO = new TransactionDTO();
        transactionDTO.setId(USER_1_TRANSACTION_ID);
    }
}
