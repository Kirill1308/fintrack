package com.popov.fintrack.report.formatter.xlsx;

import com.popov.fintrack.summary.dto.CustomSummary;
import com.popov.fintrack.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomXlsxFormatter extends BaseXlsxFormatter {

    private final WalletService walletService;

    public byte[] format(CustomSummary customSummary) {
        return null;
    }
}
