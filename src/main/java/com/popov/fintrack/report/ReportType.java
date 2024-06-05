package com.popov.fintrack.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportType {
    PDF,
    CSV,
    XLSX
}
