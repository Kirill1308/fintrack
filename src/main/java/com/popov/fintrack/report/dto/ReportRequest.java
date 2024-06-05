package com.popov.fintrack.report.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Month;

@Getter
@Setter
public class ReportRequest {
    private Long walletId;
    private int year;
    private Month month;
    private String format;
}
