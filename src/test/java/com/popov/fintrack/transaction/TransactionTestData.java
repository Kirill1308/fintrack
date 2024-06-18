package com.popov.fintrack.transaction;

import com.popov.fintrack.MatcherFactory;
import com.popov.fintrack.transaction.dto.TransactionDTO;
import com.popov.fintrack.transaction.model.Transaction;

public class TransactionTestData {

    public static final MatcherFactory.Matcher<TransactionDTO> TRANSACTION_DTO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(TransactionDTO.class);

    public static final long TRANSACTION_ID = 1;

    public static final Transaction transaction;
    public static final TransactionDTO transactionDTO;

    static {
        transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);

        transactionDTO = new TransactionDTO();
        transactionDTO.setId(TRANSACTION_ID);
    }
}
