package com.popov.fintrack.budget;

import com.popov.fintrack.budget.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query("SELECT COUNT(b) > 0 FROM Budget b WHERE b.id = :budgetId AND b.user.id = :userId")
    boolean existsByIdAndUserId(@Param("budgetId") Long budgetId, @Param("userId") Long userId);

    List<Budget> findByUserId(Long userId);
}
