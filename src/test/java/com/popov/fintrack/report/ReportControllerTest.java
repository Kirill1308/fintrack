package com.popov.fintrack.report;

import com.popov.fintrack.AbstractControllerTest;
import com.popov.fintrack.validation.WithMockJwtUser;
import com.popov.fintrack.report.service.ReportService;
import com.popov.fintrack.transaction.dto.DateRange;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.Month;

import static com.popov.fintrack.report.ReportTestData.customReportRequest;
import static com.popov.fintrack.report.ReportTestData.reportRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReportControllerTest extends AbstractControllerTest {

    @MockBean
    private ReportService reportService;

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void getYearlyReport_success() throws Exception {
        byte[] reportContent = "PDF content".getBytes();

        given(reportService.createYearlyReport(eq("pdf"), anyInt(), anyLong())).willReturn(reportContent);

        mockMvc.perform(post("/api/v1/reports/yearly")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment;filename=yearly_report_2024.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(reportContent));
    }

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void getMonthlyReport_success() throws Exception {
        byte[] reportContent = "PDF content".getBytes();

        given(reportService.createMonthlyReport(eq("pdf"), anyInt(), eq(Month.JANUARY), anyLong())).willReturn(reportContent);

        mockMvc.perform(post("/api/v1/reports/monthly")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment;filename=monthly_report_2024.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(reportContent));
    }


    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void getCustomReport_success() throws Exception {
        byte[] reportContent = "PDF content".getBytes();

        given(reportService.createCustomReport(eq("pdf"), any(DateRange.class), anyLong())).willReturn(reportContent);

        mockMvc.perform(post("/api/v1/reports/custom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customReportRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment;filename=custom_report_2024.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(reportContent));
    }
}
