package com.popov.fintrack.report;

import com.popov.fintrack.report.dto.CustomReportRequest;
import com.popov.fintrack.report.dto.ReportRequest;
import com.popov.fintrack.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private static final String ATTACHMENT = "attachment;filename=%s_report_%s.%s";
    private final ReportService reportService;

    @PostMapping("/yearly")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#reportRequest.walletId)")
    public ResponseEntity<byte[]> getYearlyReport(@RequestBody ReportRequest reportRequest) {
        validateFormat(reportRequest.getFormat());
        byte[] contents = reportService.createYearlyReport(reportRequest.getFormat(), reportRequest.getYear(), reportRequest.getWalletId());

        HttpHeaders headers = createHeaders("yearly", reportRequest.getYear(), reportRequest.getFormat());
        return ResponseEntity.ok().headers(headers).contentType(getMediaType(reportRequest.getFormat())).body(contents);
    }

    @PostMapping("/monthly")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#reportRequest.walletId)")
    public ResponseEntity<byte[]> getMonthlyReport(@RequestBody ReportRequest reportRequest) {
        validateFormat(reportRequest.getFormat());
        byte[] contents = reportService.createMonthlyReport(reportRequest.getFormat(), reportRequest.getYear(), reportRequest.getMonth(), reportRequest.getWalletId());

        HttpHeaders headers = createHeaders("monthly", reportRequest.getYear(), reportRequest.getFormat());
        return ResponseEntity.ok().headers(headers).contentType(getMediaType(reportRequest.getFormat())).body(contents);
    }

    @PostMapping("/custom")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#reportRequest.walletId)")
    public ResponseEntity<byte[]> getCustomReport(@RequestBody CustomReportRequest reportRequest) {
        validateFormat(reportRequest.getFormat());
        byte[] contents = reportService.createCustomReport(reportRequest.getFormat(), reportRequest.getDateRange(), reportRequest.getWalletId());

        HttpHeaders headers = createHeaders("custom", reportRequest.getDateRange().getStartDate().getYear(), reportRequest.getFormat());
        return ResponseEntity.ok().headers(headers).contentType(getMediaType(reportRequest.getFormat())).body(contents);
    }

    private void validateFormat(String format) {
        if (!format.equalsIgnoreCase("pdf") && !format.equalsIgnoreCase("xlsx")) {
            throw new IllegalArgumentException("Invalid report format. Must be 'pdf', 'xlsx'.");
        }
    }

    private HttpHeaders createHeaders(String reportName, int reportYear, String type) {
        HttpHeaders headers = new HttpHeaders();
        String headerValue = String.format(ATTACHMENT, reportName, reportYear, type);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, headerValue);
        return headers;
    }

    private MediaType getMediaType(String format) {
        return switch (format.toLowerCase()) {
            case "pdf" -> MediaType.APPLICATION_PDF;
            case "xlsx" -> new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            default -> throw new UnsupportedOperationException("Unsupported report format.");
        };
    }
}
