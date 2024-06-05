package com.popov.fintrack.budget;

import com.popov.fintrack.budget.dto.BudgetDTO;
import com.popov.fintrack.budget.model.Budget;
import com.popov.fintrack.utills.validation.OnCreate;
import com.popov.fintrack.utills.validation.OnUpdate;
import com.popov.fintrack.web.mapper.BudgetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final BudgetMapper budgetMapper;

    @GetMapping("/{budgetId}")
    @PreAuthorize("@customSecurityExpression.hasAccessToBudget(#budgetId)")
    public BudgetDTO getBudgetById(@PathVariable Long budgetId) {
        Budget budget = budgetService.getBudgetById(budgetId);
        return budgetMapper.toDto(budget);
    }

    @PostMapping
    public BudgetDTO createBudget(final @Validated(OnCreate.class)
                                  @RequestBody BudgetDTO budgetDTO) {
        Budget budget = budgetMapper.toEntity(budgetDTO);
        Budget createdBudget = budgetService.createBudget(budget);
        return budgetMapper.toDto(createdBudget);
    }

    @PutMapping
    @PreAuthorize("@customSecurityExpression.hasAccessToBudget(#budgetDTO.id)")
    public BudgetDTO updateBudget(final @Validated(OnUpdate.class)
                                  @RequestBody BudgetDTO budgetDTO) {
        Budget budget = budgetMapper.toEntity(budgetDTO);
        Budget createdBudget = budgetService.updateBudget(budget);
        return budgetMapper.toDto(createdBudget);
    }

    @DeleteMapping("/{budgetId}")
    @PreAuthorize("@customSecurityExpression.hasAccessToBudget(#budgetId)")
    public void deleteBudget(@PathVariable Long budgetId) {
        budgetService.deleteBudget(budgetId);
    }
}
