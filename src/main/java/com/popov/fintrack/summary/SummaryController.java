package com.popov.fintrack.summary;

import com.popov.fintrack.summary.dto.CustomSummary;
import com.popov.fintrack.summary.dto.FinancialSummary;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.summary.dto.YearlySummary;
import com.popov.fintrack.transaction.dto.FilterDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Month;

@Slf4j
@RestController
@RequestMapping("/api/v1/summaries")
@RequiredArgsConstructor
@Tag(name = "Summary Controller", description = "API for retrieving financial summaries")
public class SummaryController {

    private final SummaryService summaryService;

    @Operation(summary = "Get financial summary based on filters")
    @ApiResponses(value = {
            @ApiResponse(description = "Financial Summary", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = FinancialSummary.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/summary")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallets(#filters.walletIds)")
    public FinancialSummary getFinancialSummary(@RequestBody @Validated FilterDTO filters) {
        log.info("Request to get financial summary with filters: {}", filters);
        FinancialSummary financialSummary = summaryService.getFinancialSummary(filters);
        log.info("Retrieved financial summary: {}", financialSummary);
        return financialSummary;
    }

    @Operation(summary = "Get custom summary based on filters")
    @ApiResponses(value = {
            @ApiResponse(description = "Custom Summary", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CustomSummary.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/custom-summary")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallets(#filters.walletIds)")
    public CustomSummary getCustomSummary(@RequestBody @Validated FilterDTO filters) {
        log.info("Request to get custom summary with filters: {}", filters);
        CustomSummary customSummary = summaryService.getCustomSummary(filters);
        log.info("Retrieved custom summary: {}", customSummary);
        return customSummary;
    }

    @Operation(summary = "Get yearly summary for a wallet")
    @ApiResponses(value = {
            @ApiResponse(description = "Yearly Summary", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = YearlySummary.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/wallets/{walletId}/yearly-summary")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public YearlySummary getYearlySummary(
            @Parameter(description = "ID of the wallet", example = "1") @PathVariable Long walletId,
            @Parameter(description = "Year for the summary", example = "2023") @RequestParam int year) {
        log.info("Request to get yearly summary for wallet ID: {}, year: {}", walletId, year);
        YearlySummary yearlySummary = summaryService.getYearlySummary(year, walletId);
        log.info("Retrieved yearly summary for wallet ID: {}, year: {}", walletId, year);
        return yearlySummary;
    }

    @Operation(summary = "Get monthly summary for a wallet")
    @ApiResponses(value = {
            @ApiResponse(description = "Monthly Summary", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = MonthlySummary.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/wallets/{walletId}/monthly-summary")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public MonthlySummary getMonthlySummary(
            @Parameter(description = "ID of the wallet", example = "1") @PathVariable Long walletId,
            @Parameter(description = "Year for the summary", example = "2023") @RequestParam int year,
            @Parameter(description = "Month for the summary", example = "JANUARY") @RequestParam Month month) {
        log.info("Request to get monthly summary for wallet ID: {}, year: {}, month: {}", walletId, year, month);
        MonthlySummary monthlySummary = summaryService.getMonthlySummary(year, month, walletId);
        log.info("Retrieved monthly summary for wallet ID: {}, year: {}, month: {}", walletId, year, month);
        return monthlySummary;
    }
}
