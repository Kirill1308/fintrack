package com.popov.fintrack.report.repository;

import com.popov.fintrack.transaction.TransactionRepository;
import com.popov.fintrack.transaction.model.Transaction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends TransactionRepository {

    @Query(value = """
            SELECT SUM(t.amount)
                FROM Transaction t
                WHERE t.walletId = :walletId
                AND t.type = 'INCOME'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
            """)
    Double getTotalIncomeForYear(@Param("year") int year, @Param("walletId") Long walletId);

    @Query(value = """
            SELECT EXTRACT(MONTH FROM t.dateCreated), SUM(t.amount) as total
                FROM Transaction t
                WHERE t.walletId = :walletId
                AND t.type = 'INCOME'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                GROUP BY EXTRACT(MONTH FROM t.dateCreated)
                ORDER BY total DESC
            """)
    List<Object[]> getHighestIncomeMonth(@Param("year") int year, @Param("walletId") Long walletId);

    @Query(value = """
            SELECT EXTRACT(MONTH FROM t.dateCreated), SUM(t.amount) as total
                FROM Transaction t
                WHERE t.walletId = :walletId
                AND t.type = 'INCOME'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                GROUP BY EXTRACT(MONTH FROM t.dateCreated)
                ORDER BY total ASC
            """)
    List<Object[]> getLowestIncomeMonth(@Param("year") int year, @Param("walletId") Long walletId);

    @Query(value = """
            SELECT AVG(t.amount)
                FROM Transaction t
                WHERE t.walletId = :walletId
                AND t.type = 'INCOME'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
            """)
    Double getAverageMonthlyIncome(@Param("year") int year, @Param("walletId") Long walletId);

    @Query(value = """
            SELECT EXTRACT(MONTH FROM t.dateCreated) AS month, SUM(t.amount) AS total
                FROM Transaction t
                WHERE t.walletId = :walletId
                AND t.type = 'INCOME'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                GROUP BY EXTRACT(MONTH FROM t.dateCreated)
            """)
    List<Object[]> getIncomesPerMonth(@Param("year") int year, @Param("walletId") Long walletId);

    @Query(value = """
            SELECT t.category, SUM(t.amount) AS total
                FROM Transaction t
                WHERE t.walletId = :walletId
                AND t.type = 'INCOME'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                GROUP BY t.category
            """)
    List<Object[]> getIncomePerCategory(@Param("year") int year, @Param("walletId") Long walletId);

    @Query(value = """
            SELECT SUM(t.amount)
                FROM Transaction t
                WHERE t.walletId = :walletId
                AND t.type = 'INCOME'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                AND EXTRACT(MONTH FROM t.dateCreated) = :monthValue
            """)
    Double getTotalIncomeForMonth(@Param("year") int year, @Param("monthValue") int monthValue, @Param("walletId") Long walletId);

    @Query(value = """
            SELECT AVG(t.amount)
                FROM Transaction t
                WHERE t.walletId = :walletId
                AND t.type = 'INCOME'
                AND EXTRACT(YEAR FROM t.dateCreated) = :year
                AND EXTRACT(MONTH FROM t.dateCreated) = :monthValue
            """)
    Double getLowestDailyIncome(@Param("year") int year, @Param("monthValue") int monthValue, @Param("walletId") Long walletId);

    @Query(value = """
            SELECT t.dateCreated, SUM(t.amount)
                FROM Transaction t
                WHERE t.walletId = :walletId
                AND t.type = 'INCOME'
                AND t.dateCreated BETWEEN :startDate AND :endDate
                GROUP BY t.dateCreated
            """)
    List<Object[]> getIncomesPerPeriod(LocalDate startDate, LocalDate endDate, Long walletId);

    default List<Object[]> getIncomePerCategory(Specification<Transaction> spec) {
        return findAll(spec).stream()
                .map(transaction -> new Object[]{transaction.getCategory(), transaction.getAmount()})
                .toList();
    }
}
