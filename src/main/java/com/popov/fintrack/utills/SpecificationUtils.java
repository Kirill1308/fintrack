package com.popov.fintrack.utills;

import com.popov.fintrack.transaction.dto.AmountRange;
import com.popov.fintrack.transaction.dto.DateRange;
import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.transaction.model.Category;
import com.popov.fintrack.transaction.model.Transaction;
import com.popov.fintrack.transaction.model.Type;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpecificationUtils {

    public static Specification<Transaction> buildSpecification(FilterDTO filters, Type transactionType) {
        Specification<Transaction> spec = Specification.where(null);

        spec = SpecificationUtils.applyUserFilter(spec, filters.getUserIds());
        spec = SpecificationUtils.applyWalletFilter(spec, filters.getWalletIds());
        spec = SpecificationUtils.applyDateRangeFilter(spec, filters.getDateRange());
        spec = SpecificationUtils.applyCategoryFilter(spec, filters.getCategories());
        spec = SpecificationUtils.applyAmountRangeFilter(spec, filters.getAmountRange());
        spec = SpecificationUtils.applyNoteFilter(spec, filters.getNote());

        if (transactionType != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), transactionType.name()));
        }

        return spec;
    }

    public static Specification<Transaction> applyUserFilter(Specification<Transaction> spec, List<Long> userIds) {
        if (userIds != null && !userIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("owner").get("id").in(userIds));
        }
        return spec;
    }

    public static Specification<Transaction> applyWalletFilter(Specification<Transaction> spec, List<Long> walletIds) {
        if (walletIds != null && !walletIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("wallet").get("id").in(walletIds));
        }
        return spec;
    }

    public static Specification<Transaction> applyCategoryFilter(Specification<Transaction> spec, List<Category> categories) {
        if (categories != null && !categories.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("category").in(categories));
        }
        return spec;
    }

    public static Specification<Transaction> applyAmountRangeFilter(Specification<Transaction> spec, AmountRange amountRange) {
        if (amountRange != null) {
            if (amountRange.getMin() != null) {
                spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("amount"), amountRange.getMin()));
            }
            if (amountRange.getMax() != null) {
                spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("amount"), amountRange.getMax()));
            }
        }
        return spec;
    }

    public static Specification<Transaction> applyNoteFilter(Specification<Transaction> spec, String note) {
        if (note != null) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("note"), "%" + note + "%"));
        }
        return spec;
    }

    public static Specification<Transaction> applyDateRangeFilter(Specification<Transaction> spec, DateRange dateRange) {
        if (dateRange != null) {
            if (dateRange.getStartDate() != null) {
                spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dateCreated"), dateRange.getStartDate()));
            }
            if (dateRange.getEndDate() != null) {
                spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("dateCreated"), dateRange.getEndDate()));
            }
        }
        return spec;
    }
}
