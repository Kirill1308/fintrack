package com.popov.fintrack.budget;

import com.popov.fintrack.MatcherFactory;
import com.popov.fintrack.budget.dto.BudgetDTO;
import com.popov.fintrack.budget.model.Budget;
import com.popov.fintrack.transaction.model.Category;

import java.time.LocalDate;
import java.util.List;

import static com.popov.fintrack.user.UserTestData.user;
import static com.popov.fintrack.wallet.WalletTestData.wallet;

public class BudgetTestData {

    public static final MatcherFactory.Matcher<BudgetDTO> BUDGET_DTO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(BudgetDTO.class);

    public static final long OWNED_BUDGET_ID = 2L;
    public static final long ANOTHER_USERS_BUDGET_ID = 1L;

    public static final Budget budget;
    public static final BudgetDTO budgetDTO;

    static {
        budget = new Budget();
        budget.setId(OWNED_BUDGET_ID);
        budget.setOwner(user);
        budget.setWallets(List.of(wallet));
        budget.setName("Vacation Budget");
        budget.setBudgetedAmount(2000.0);
        budget.setCategory(Category.TRAVEL);
        budget.setCurrency("USD");
        budget.setStartDate(LocalDate.parse("2024-01-01"));
        budget.setEndDate(LocalDate.parse("2024-06-01"));

        budgetDTO = new BudgetDTO();
        budgetDTO.setId(OWNED_BUDGET_ID);
        budgetDTO.setName("Vacation Budget");
        budgetDTO.setBudgetedAmount(2000.0);
        budgetDTO.setCategory(Category.TRAVEL);
        budgetDTO.setCurrency("USD");
        budgetDTO.setStartDate(LocalDate.parse("2024-01-01"));
        budgetDTO.setEndDate(LocalDate.parse("2024-06-01"));
    }
}
