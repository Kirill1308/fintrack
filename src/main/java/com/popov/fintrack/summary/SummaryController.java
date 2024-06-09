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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;

@RestController
@RequestMapping("/api/v1/summaries")
@RequiredArgsConstructor
@Tag(name = "Summary Controller", description = "API for retrieving financial summaries")
public class SummaryController {

    private final SummaryService summaryService;

    @Operation(summary = "Get financial summary based on filters",
            responses = {
                    @ApiResponse(description = "Financial summary", responseCode = "200", content = @Content(schema = @Schema(implementation = FinancialSummary.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            })
    @PostMapping("/summary")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallets(#filters.walletIds)")
    public FinancialSummary getFinancialSummary(@RequestBody @Validated FilterDTO filters) {
        return summaryService.getFinancialSummary(filters);
    }

    @Operation(summary = "Get custom summary based on filters",
            responses = {
                    @ApiResponse(description = "Custom summary", responseCode = "200", content = @Content(schema = @Schema(implementation = CustomSummary.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            })
    @PostMapping("/custom-summary")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallets(#filters.walletIds)")
    public CustomSummary getCustomSummary(@RequestBody @Validated FilterDTO filters) {
        return summaryService.getCustomSummary(filters);
    }

    @Operation(summary = "Get yearly summary for a wallet",
            responses = {
                    @ApiResponse(description = "Yearly summary", responseCode = "200", content = @Content(schema = @Schema(implementation = YearlySummary.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            })
    @GetMapping("/wallets/{walletId}/yearly-summary")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public YearlySummary getYearlySummary(
            @Parameter(description = "ID of the wallet", example = "1") @PathVariable Long walletId,
            @Parameter(description = "Year for the summary", example = "2023") @RequestParam int year) {
        return summaryService.getYearlySummary(year, walletId);
    }

    @Operation(summary = "Get monthly summary for a wallet",
            responses = {
                    @ApiResponse(description = "Monthly summary", responseCode = "200", content = @Content(schema = @Schema(implementation = MonthlySummary.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            })
    @GetMapping("/wallets/{walletId}/monthly-summary")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public MonthlySummary getMonthlySummary(
            @Parameter(description = "ID of the wallet", example = "1") @PathVariable Long walletId,
            @Parameter(description = "Year for the summary", example = "2023") @RequestParam int year,
            @Parameter(description = "Month for the summary", example = "JANUARY") @RequestParam Month month) {
        return summaryService.getMonthlySummary(year, month, walletId);
    }
}
