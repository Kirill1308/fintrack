package com.popov.fintrack.budget.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.popov.fintrack.budget.model.BudgetStatus;
import com.popov.fintrack.transaction.model.Category;
import com.popov.fintrack.utills.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BudgetDTO {

    @Schema(description = "Unique identifier of the budget", example = "1", accessMode = Schema.AccessMode.READ_WRITE)
    @NotNull(message = "Id cannot be null", groups = {OnUpdate.class})
    private Long id;

    @Schema(description = "List of wallet IDs associated with the budget", accessMode = Schema.AccessMode.WRITE_ONLY, example = "[1, 2, 3]")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Long> walletIds;

    @Schema(description = "Name of the budget", example = "Monthly Budget")
    @NotNull(message = "Name cannot be null")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @Schema(description = "Initial budgeted amount", example = "1000.0")
    @NotNull(message = "Initial amount cannot be null")
    @DecimalMin(value = "0.0", message = "Initial amount must be greater than 0")
    private Double budgetedAmount;

    @Schema(description = "Amount already spent from the budget", example = "200.0", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double spentAmount = 0.0;

    @Schema(description = "Remaining amount in the budget", example = "800.0", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double remainingAmount = 0.0;

    @Schema(description = "Available amount per day", example = "26.67", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double availableAmountPerDay = 0.0;

    @Schema(description = "Status of the budget", example = "ACTIVE", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BudgetStatus status = BudgetStatus.ACTIVE;

    @Schema(description = "Currency of the budget", example = "USD")
    @NotNull(message = "Currency cannot be null")
    @Size(max = 255, message = "Currency must be less than 255 characters")
    private String currency;

    @Schema(description = "Category of the budget")
    @NotNull(message = "Category cannot be null")
    private Category category;

    @Schema(description = "Start date of the budget", example = "2023-01-01")
    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @Schema(description = "End date of the budget", example = "2023-12-31")
    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;
}
