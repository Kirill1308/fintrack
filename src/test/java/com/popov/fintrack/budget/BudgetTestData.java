package com.popov.fintrack.budget;

import com.popov.fintrack.MatcherFactory;
import com.popov.fintrack.budget.dto.BudgetDTO;
import com.popov.fintrack.budget.model.Budget;

public class BudgetTestData {

    public static final MatcherFactory.Matcher<BudgetDTO> BUDGET_DTO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(BudgetDTO.class);

    public static final Budget budget;
    public static final BudgetDTO budgetDTO;

    static {
        budget = new Budget();
        budget.setId(1L);

        budgetDTO = new BudgetDTO();
        budgetDTO.setId(1L);
    }
}
