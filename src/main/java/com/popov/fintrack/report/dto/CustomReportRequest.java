package com.popov.fintrack.report.dto;

import com.popov.fintrack.transaction.dto.DateRange;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomReportRequest {
    private Long walletId;
    private DateRange dateRange;
    private String format;
}
