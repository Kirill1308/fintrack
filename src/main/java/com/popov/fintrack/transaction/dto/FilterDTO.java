package com.popov.fintrack.transaction.dto;

import com.popov.fintrack.transaction.model.Category;
import lombok.Data;

import java.util.List;

@Data
public class FilterDTO {
    private List<Long> userIds;
    private List<Long> walletIds;
    private List<Category> categories;
    private DateRange dateRange;
    private AmountRange amountRange;
    private String note;
}
