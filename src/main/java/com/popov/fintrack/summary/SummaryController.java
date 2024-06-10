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
        return summaryService.getFinancialSummary(filters);
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
        return summaryService.getCustomSummary(filters);
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
        return summaryService.getYearlySummary(year, walletId);
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
        return summaryService.getMonthlySummary(year, month, walletId);
    }
}
