package com.popov.fintrack.report.repository;

import com.popov.fintrack.transaction.TransactionRepository;
import com.popov.fintrack.transaction.model.Transaction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends TransactionRepository {

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.walletId = :walletId AND t.type = 'EXPENSE' AND EXTRACT(YEAR FROM t.dateCreated) = :year")
    Double getTotalExpensesForYear(@Param("year") int year, @Param("walletId") Long walletId);

    @Query(value = """
            SELECT EXTRACT(MONTH FROM t.dateCreated) as month, SUM(t.amount) as total
                FROM Transaction t
                WHERE t.walletId = :walletId
                AND t.type = 'EXPENSE'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                GROUP BY month
                ORDER BY total DESC
            """)
    List<Object[]> getMostExpensiveMonth(@Param("year") int year, @Param("walletId") Long walletId);

    @Query(value = """
            SELECT EXTRACT(MONTH FROM t.dateCreated) as month, SUM(t.amount) as total
                FROM Transaction t
                WHERE t.walletId = :walletId
                AND t.type = 'EXPENSE'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                GROUP BY month
                ORDER BY total ASC
            """)
    List<Object[]> getLeastExpensiveMonth(@Param("year") int year, @Param("walletId") Long walletId);

    @Query(value = """
            SELECT AVG(monthly_totals.total)
                FROM (
                    SELECT EXTRACT(MONTH FROM t.dateCreated) as month, SUM(t.amount) as total
                        FROM Transaction t
                        WHERE t.walletId = :walletId
                        AND t.type = 'EXPENSE'
                        AND EXTRACT(YEAR FROM t.dateCreated) = :year
                        GROUP BY month
                ) as monthly_totals
            """)
    Double getAverageMonthlyExpense(@Param("year") int year, @Param("walletId") Long walletId);

    @Query(value = """
            SELECT EXTRACT(MONTH FROM t.dateCreated) as month, SUM(t.amount) as total
                FROM Transaction t
                WHERE t.walletId = :walletId
                AND t.type = 'EXPENSE'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                GROUP BY EXTRACT(MONTH FROM t.dateCreated)
            """)
    List<Object[]> getExpensesPerMonth(@Param("year") int year, @Param("walletId") Long walletId);

    @Query(value = """
            SELECT t.category, SUM(t.amount) as total
                FROM Transaction t
                WHERE t.walletId = :walletId
                AND t.type = 'EXPENSE'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                GROUP BY t.category
            """)
    List<Object[]> getExpensesPerCategory(@Param("year") int year, @Param("walletId") Long walletId);

    @Query(value = """
            SELECT SUM(t.amount)
                FROM Transaction t
                WHERE t.walletId = :walletId
                AND t.type = 'EXPENSE'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                AND EXTRACT(MONTH FROM t.dateCreated) = :monthValue
            """)
    Double getTotalExpensesForMonth(@Param("year") int year, @Param("monthValue") int monthValue, @Param("walletId") Long walletId);

    @Query(value = """
            SELECT AVG(t.amount)
                FROM Transaction t
                WHERE t.walletId = :walletId
                AND t.type = 'EXPENSE'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                AND EXTRACT(MONTH FROM t.dateCreated) = :monthValue
            """)
    Double getAverageDailyExpense(@Param("year") int year, @Param("monthValue") int monthValue, @Param("walletId") Long walletId);

    @Query(value = """
            SELECT MAX(t.amount)
                FROM Transaction t
                WHERE t.walletId = :walletId
                AND t.type = 'EXPENSE'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                AND EXTRACT(MONTH FROM t.dateCreated) = :monthValue
            """)
    Double getHighestDailyExpense(@Param("year") int year, @Param("monthValue") int monthValue, @Param("walletId") Long walletId);

    default Double getAverageDailyExpense(Specification<Transaction> spec) {
        return findAll(spec).stream()
                .mapToDouble(Transaction::getAmount)
                .average()
                .orElse(0.0);
    }}
