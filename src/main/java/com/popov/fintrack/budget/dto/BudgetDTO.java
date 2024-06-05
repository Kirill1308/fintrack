package com.popov.fintrack.budget.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.popov.fintrack.budget.model.BudgetStatus;
import com.popov.fintrack.transaction.model.Category;
import com.popov.fintrack.validation.OnUpdate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BudgetDTO {

    @NotNull(message = "Id cannot be null", groups = {OnUpdate.class})
    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotNull(message = "Initial amount cannot be null")
    @DecimalMin(value = "0.0", message = "Initial amount must be greater than 0")
    private Double budgetedAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double spentAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double remainingAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double availableAmountPerDay;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BudgetStatus status;

    @NotNull(message = "Currency cannot be null")
    @Size(max = 255, message = "Currency must be less than 255 characters")
    private String currency;

    @NotNull(message = "Category cannot be null")
    private Category category;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;
}
