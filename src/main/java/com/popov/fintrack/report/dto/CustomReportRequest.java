package com.popov.fintrack.report.dto;

import com.popov.fintrack.transaction.dto.DateRange;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomReportRequest {
    private Long walletId;
    private DateRange dateRange;
    private String format;
}
