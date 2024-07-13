package com.popov.fintrack.transaction;

import com.popov.fintrack.MatcherFactory;
import com.popov.fintrack.transaction.dto.TransactionDTO;
import com.popov.fintrack.transaction.model.Category;
import com.popov.fintrack.transaction.model.PaymentMethod;
import com.popov.fintrack.transaction.model.Transaction;
import com.popov.fintrack.transaction.model.Type;

import java.time.LocalDate;

import static com.popov.fintrack.user.UserTestData.user;
import static com.popov.fintrack.wallet.WalletTestData.wallet;

public class TransactionTestData {

    public static final MatcherFactory.Matcher<TransactionDTO> TRANSACTION_DTO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(TransactionDTO.class);

    public static final MatcherFactory.Matcher<Transaction> TRANSACTION_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Transaction.class);

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

    public static Transaction getNew() {
        Transaction newTransaction = new Transaction();
        newTransaction.setOwner(user);
        newTransaction.setType(Type.EXPENSE);
        newTransaction.setCategory(Category.FOOD);
        newTransaction.setPayment(PaymentMethod.CASH);
        newTransaction.setAmount(50.0);
        newTransaction.setCurrency("USD");
        newTransaction.setNote("Lunch");
        newTransaction.setDateCreated(LocalDate.now());
        newTransaction.setDateUpdated(LocalDate.now());
        return newTransaction;
    }

    public static Transaction getUpdated() {
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setId(USER_1_TRANSACTION_ID);
        updatedTransaction.setOwner(user);
        updatedTransaction.setWallet(wallet);
        updatedTransaction.setType(Type.EXPENSE);
        updatedTransaction.setCategory(Category.FOOD);
        updatedTransaction.setPayment(PaymentMethod.CASH);
        updatedTransaction.setAmount(500.0);
        updatedTransaction.setCurrency("USD");
        updatedTransaction.setNote("Lunch");
        updatedTransaction.setDateCreated(LocalDate.now());
        updatedTransaction.setDateUpdated(LocalDate.now());
        return updatedTransaction;
    }
}
