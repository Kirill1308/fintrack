package com.popov.fintrack.transaction;

import com.popov.fintrack.MatcherFactory;
import com.popov.fintrack.transaction.dto.TransactionDTO;
import com.popov.fintrack.transaction.model.Category;
import com.popov.fintrack.transaction.model.PaymentMethod;
import com.popov.fintrack.transaction.model.Transaction;
import com.popov.fintrack.transaction.model.Type;

import java.time.LocalDate;

import static com.popov.fintrack.user.UserTestData.user;

public class TransactionTestData {

    public static final MatcherFactory.Matcher<TransactionDTO> TRANSACTION_DTO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(TransactionDTO.class);

    public static final Long USER_1_TRANSACTION_ID = 2L;

    public static final Transaction transaction;
    public static final TransactionDTO transactionDTO;

    static {
        transaction = new Transaction();
        transaction.setId(USER_1_TRANSACTION_ID);
        transaction.setOwner(user);
        transaction.setType(Type.INCOME);
        transaction.setCategory(Category.SALARY);
        transaction.setPayment(PaymentMethod.BANK_TRANSFER);
        transaction.setAmount(1500.0);
        transaction.setCurrency("USD");
        transaction.setNote("Monthly salary");
        transaction.setDateCreated(LocalDate.now());
        transaction.setDateUpdated(LocalDate.now());

        transactionDTO = new TransactionDTO();
        transactionDTO.setId(USER_1_TRANSACTION_ID);
        transactionDTO.setType("INCOME");
        transactionDTO.setCategory("SALARY");
        transactionDTO.setPayment("BANK_TRANSFER");
        transactionDTO.setAmount(1500.0);
        transactionDTO.setCurrency("USD");
        transactionDTO.setNote("Monthly salary");
    }
}
