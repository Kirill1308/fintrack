package com.popov.fintrack.budget;

import com.popov.fintrack.budget.dto.BudgetDTO;
import com.popov.fintrack.budget.model.Budget;
import com.popov.fintrack.utills.validation.OnCreate;
import com.popov.fintrack.utills.validation.OnUpdate;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.model.Wallet;
import com.popov.fintrack.web.mapper.BudgetMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/budgets")
@Tag(name = "Budget Controller", description = "API for managing budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final WalletService walletService;
    private final BudgetMapper budgetMapper;

    @Operation(summary = "Get Budget by ID", description = "Get details of a budget by its ID")
    @ApiResponses(value = {
            @ApiResponse(description = "Budget Details", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BudgetDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{budgetId}")
    @PreAuthorize("@customSecurityExpression.hasAccessToBudget(#budgetId)")
    public BudgetDTO getBudgetById(
            @Parameter(description = "ID of the budget to retrieve", required = true)
            @PathVariable Long budgetId) {
        Budget budget = budgetService.getBudgetById(budgetId);
        return budgetMapper.toDto(budget);
    }

    @Operation(summary = "Create a new Budget", description = "Create a new budget")
    @ApiResponses(value = {
            @ApiResponse(description = "Budget Details", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BudgetDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public BudgetDTO createBudget(
            @Parameter(description = "Budget data transfer object", required = true)
            @Validated(OnCreate.class) @RequestBody BudgetDTO budgetDTO) {
        Budget budget = budgetMapper.toEntity(budgetDTO);
        List<Wallet> wallets = walletService.getWalletsByIds(budgetDTO.getWalletIds());
        budget.setWallets(wallets);

        Budget createdBudget = budgetService.updateBudget(budget);
        return budgetMapper.toDto(createdBudget);
    }

    @Operation(summary = "Update an existing Budget", description = "Update the details of an existing budget")
    @ApiResponses(value = {
            @ApiResponse(description = "Budget Details", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BudgetDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping
    @PreAuthorize("@customSecurityExpression.hasAccessToBudget(#budgetDTO.id)")
    public BudgetDTO updateBudget(
            @Parameter(description = "Budget data transfer object", required = true)
            @Validated(OnUpdate.class) @RequestBody BudgetDTO budgetDTO) {
        Budget budget = budgetMapper.toEntity(budgetDTO);
        Budget createdBudget = budgetService.updateBudget(budget);
        return budgetMapper.toDto(createdBudget);
    }

    @Operation(summary = "Delete a Budget by ID", description = "Delete a budget by its ID")
    @ApiResponses(value = {
            @ApiResponse(description = "Budget deleted successfully", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BudgetDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{budgetId}")
    @PreAuthorize("@customSecurityExpression.hasAccessToBudget(#budgetId)")
    public void deleteBudget(
            @Parameter(description = "ID of the budget to delete", required = true)
            @PathVariable Long budgetId) {
        budgetService.deleteBudget(budgetId);
    }
}
