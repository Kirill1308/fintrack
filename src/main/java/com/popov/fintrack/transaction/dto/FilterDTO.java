package com.popov.fintrack.transaction.dto;

import com.popov.fintrack.transaction.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "DTO for filtering financial data based on various criteria.")
public class FilterDTO {

    @Schema(description = "List of user IDs to filter by", example = "[1, 2, 3]")
    private List<Long> userIds;

    @Schema(description = "List of wallet IDs to filter by", example = "[101, 102, 103]")
    private List<Long> walletIds;

    @Schema(description = "List of categories to filter by")
    private List<Category> categories;

    @Schema(description = "Date range for the filter")
    private DateRange dateRange;

    @Schema(description = "Amount range for the filter")
    private AmountRange amountRange;

    @Schema(description = "Note to filter by", example = "Groceries")
    private String note;
}
