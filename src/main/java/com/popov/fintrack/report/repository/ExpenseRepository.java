package com.popov.fintrack.report.repository;

import com.popov.fintrack.transaction.TransactionRepository;
import com.popov.fintrack.transaction.model.Transaction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends TransactionRepository {

    @Query(value = """
            SELECT SUM(t.amount) 
            FROM Transaction t 
            WHERE t.wallet.id = :walletId 
            AND t.type = 'EXPENSE' 
            AND EXTRACT(YEAR FROM t.dateCreated) = :year
            """)
    Double getTotalExpensesForYear(int year, Long walletId);

    @Query(value = """
            SELECT EXTRACT(MONTH FROM t.dateCreated) as month, SUM(t.amount) as total
                FROM Transaction t
                WHERE t.wallet.id = :walletId
                AND t.type = 'EXPENSE'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                GROUP BY month
                ORDER BY total DESC
            """)
    List<Object[]> getMostExpensiveMonth(int year, Long walletId);

    @Query(value = """
            SELECT EXTRACT(MONTH FROM t.dateCreated) as month, SUM(t.amount) as total
                FROM Transaction t
                WHERE t.wallet.id = :walletId
                AND t.type = 'EXPENSE'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                GROUP BY month
                ORDER BY total ASC
            """)
    List<Object[]> getLeastExpensiveMonth(int year, Long walletId);

    @Query(value = """
            SELECT AVG(monthly_totals.total)
                FROM (
                    SELECT EXTRACT(MONTH FROM t.dateCreated) as month, SUM(t.amount) as total
                        FROM Transaction t
                        WHERE t.wallet.id = :walletId
                        AND t.type = 'EXPENSE'
                        AND EXTRACT(YEAR FROM t.dateCreated) = :year
                        GROUP BY month
                ) as monthly_totals
            """)
    Double getAverageMonthlyExpense(int year, Long walletId);

    @Query(value = """
            SELECT EXTRACT(MONTH FROM t.dateCreated) as month, SUM(t.amount) as total
                FROM Transaction t
                WHERE t.wallet.id = :walletId
                AND t.type = 'EXPENSE'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                GROUP BY EXTRACT(MONTH FROM t.dateCreated)
            """)
    List<Object[]> getExpensesPerMonth(int year, Long walletId);

    @Query(value = """
            SELECT t.category, SUM(t.amount) as total
                FROM Transaction t
                WHERE t.wallet.id = :walletId
                AND t.type = 'EXPENSE'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                GROUP BY t.category
            """)
    List<Object[]> getExpensesPerCategory(int year, Long walletId);

    @Query(value = """
            SELECT SUM(t.amount)
                FROM Transaction t
                WHERE t.wallet.id = :walletId
                AND t.type = 'EXPENSE'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                AND EXTRACT(MONTH FROM t.dateCreated) = :monthValue
            """)
    Double getTotalExpensesForMonth(int year, int monthValue, Long walletId);

    @Query(value = """
            SELECT AVG(t.amount)
                FROM Transaction t
                WHERE t.wallet.id = :walletId
                AND t.type = 'EXPENSE'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                AND EXTRACT(MONTH FROM t.dateCreated) = :monthValue
            """)
    Double getAverageDailyExpense(int year, int monthValue, Long walletId);

    @Query(value = """
            SELECT MAX(t.amount)
                FROM Transaction t
                WHERE t.wallet.id = :walletId
                AND t.type = 'EXPENSE'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                AND EXTRACT(MONTH FROM t.dateCreated) = :monthValue
            """)
    Double getHighestDailyExpense(int year, int monthValue, Long walletId);

    default Double getAverageDailyExpense(Specification<Transaction> spec) {
        return findAll(spec).stream()
                .mapToDouble(Transaction::getAmount)
                .average()
                .orElse(0.0);
    }
}
