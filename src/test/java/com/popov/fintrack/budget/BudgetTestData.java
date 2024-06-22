package com.popov.fintrack.budget;

import com.popov.fintrack.MatcherFactory;
import com.popov.fintrack.budget.dto.BudgetDTO;
import com.popov.fintrack.budget.model.Budget;

import static com.popov.fintrack.user.UserTestData.user;

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

        budgetDTO = new BudgetDTO();
        budgetDTO.setId(OWNED_BUDGET_ID);
    }
}
