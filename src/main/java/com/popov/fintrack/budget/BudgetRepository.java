package com.popov.fintrack.budget;

import com.popov.fintrack.budget.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    boolean existsByIdAndUserId(Long budgetId, Long userId);

    List<Budget> findByUserId(Long userId);
}
