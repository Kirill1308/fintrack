package com.popov.fintrack.report.repository;

import com.popov.fintrack.transaction.TransactionRepository;
import com.popov.fintrack.transaction.model.Transaction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends TransactionRepository {

    @Query(value = """
            SELECT SUM(t.amount)
                FROM Transaction t
                WHERE t.wallet.id = :walletId
                AND t.type = 'INCOME'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
            """)
    Double getTotalIncomeForYear(int year, Long walletId);

    @Query(value = """
            SELECT EXTRACT(MONTH FROM t.dateCreated), SUM(t.amount) as total
                FROM Transaction t
                WHERE t.wallet.id = :walletId
                AND t.type = 'INCOME'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                GROUP BY EXTRACT(MONTH FROM t.dateCreated)
                ORDER BY total DESC
            """)
    List<Object[]> getHighestIncomeMonth(int year, Long walletId);

    @Query(value = """
            SELECT EXTRACT(MONTH FROM t.dateCreated), SUM(t.amount) as total
                FROM Transaction t
                WHERE t.wallet.id = :walletId
                AND t.type = 'INCOME'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                GROUP BY EXTRACT(MONTH FROM t.dateCreated)
                ORDER BY total ASC
            """)
    List<Object[]> getLowestIncomeMonth(int year, Long walletId);

    @Query(value = """
            SELECT AVG(t.amount)
                FROM Transaction t
                WHERE t.wallet.id = :walletId
                AND t.type = 'INCOME'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
            """)
    Double getAverageMonthlyIncome(int year, Long walletId);

    @Query(value = """
            SELECT EXTRACT(MONTH FROM t.dateCreated) AS month, SUM(t.amount) AS total
                FROM Transaction t
                WHERE t.wallet.id = :walletId
                AND t.type = 'INCOME'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                GROUP BY EXTRACT(MONTH FROM t.dateCreated)
            """)
    List<Object[]> getIncomesPerMonth(int year, Long walletId);

    @Query(value = """
            SELECT t.category, SUM(t.amount) AS total
                FROM Transaction t
                WHERE t.wallet.id = :walletId
                AND t.type = 'INCOME'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                GROUP BY t.category
            """)
    List<Object[]> getIncomePerCategory(int year, Long walletId);

    @Query(value = """
            SELECT SUM(t.amount)
                FROM Transaction t
                WHERE t.wallet.id = :walletId
                AND t.type = 'INCOME'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                AND EXTRACT(MONTH FROM t.dateCreated) = :monthValue
            """)
    Double getTotalIncomeForMonth(int year, int monthValue, Long walletId);

    @Query(value = """
            SELECT AVG(t.amount)
                FROM Transaction t
                WHERE t.wallet.id = :walletId
                AND t.type = 'INCOME'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                AND EXTRACT(MONTH FROM t.dateCreated) = :monthValue
            """)
    Double getLowestDailyIncome(int year, int monthValue, Long walletId);

    default List<Object[]> getIncomePerCategory(Specification<Transaction> spec) {
        return findAll(spec).stream()
                .map(transaction -> new Object[]{transaction.getCategory(), transaction.getAmount()})
                .toList();
    }
}
