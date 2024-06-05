package com.popov.fintrack.summary;

import com.popov.fintrack.summary.dto.CustomSummary;
import com.popov.fintrack.summary.dto.FinancialSummary;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.summary.dto.YearlySummary;
import com.popov.fintrack.transaction.dto.FilterDTO;
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
public class SummaryController {

    private final SummaryService summaryService;

    @PostMapping("/summary")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallets(#filters.walletIds)")
    public FinancialSummary getFinancialSummary(@RequestBody @Validated FilterDTO filters) {
        return summaryService.getFinancialSummary(filters);
    }

    @PostMapping("/custom-summary")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallets(#filters.walletIds)")
    public CustomSummary getCustomSummary(@RequestBody @Validated FilterDTO filters) {
        return summaryService.getCustomSummary(filters);
    }

    @GetMapping("/wallets/{walletId}/yearly-summary")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public YearlySummary getYearlySummary(@PathVariable Long walletId,
                                          @RequestParam int year) {
        return summaryService.getYearlySummary(year, walletId);
    }

    @GetMapping("/wallets/{walletId}/monthly-summary")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public MonthlySummary getMonthlySummary(@PathVariable Long walletId, @RequestParam int year,
                                            @RequestParam Month month) {
        return summaryService.getMonthlySummary(year, month, walletId);
    }
}
