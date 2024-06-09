package com.popov.fintrack.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.Month;

@Getter
@Setter
public class ReportRequest {

    @Schema(description = "ID of the wallet", example = "1")
    @NotNull(message = "Wallet ID cannot be null")
    private Long walletId;

    @Schema(description = "Year for the report", example = "2023")
    @NotNull(message = "Year cannot be null")
    private int year;

    @Schema(description = "Month for the report", example = "JANUARY")
    private Month month;

    @Schema(description = "Format of the report (pdf or xlsx)", example = "pdf")
    @Pattern(regexp = "^(pdf|xlsx)$", message = "Invalid format. Must be 'pdf' or 'xlsx'")
    @NotNull(message = "Format cannot be null")
    private String format;
}
