package com.popov.fintrack.report;

import com.popov.fintrack.AbstractControllerTest;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Arrays;

import static com.popov.fintrack.report.ReportTestData.customReportRequest;
import static com.popov.fintrack.report.ReportTestData.reportRequest;
import static com.popov.fintrack.user.UserTestData.USER_MAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReportControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("Should return yearly PDF report")
    void getYearlyReport_success() throws Exception {
        mockMvc.perform(post("/api/v1/reports/yearly")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment;filename=yearly_report_2024.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsByteArray().length > 0))
                .andExpect(result -> {
                    byte[] responseBytes = result.getResponse().getContentAsByteArray();
                    String responseHeader = new String(Arrays.copyOf(responseBytes, 5));
                    assertEquals("%PDF-", responseHeader);
                });
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("Should return monthly PDF report")
    void getMonthlyReport_success() throws Exception {
        mockMvc.perform(post("/api/v1/reports/monthly")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment;filename=monthly_report_2024.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsByteArray().length > 0))
                .andExpect(result -> {
                    byte[] responseBytes = result.getResponse().getContentAsByteArray();
                    String responseHeader = new String(Arrays.copyOf(responseBytes, 5));
                    assertEquals("%PDF-", responseHeader);
                });
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("Should return custom PDF report")
    void getCustomReport_success() throws Exception {
        mockMvc.perform(post("/api/v1/reports/custom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customReportRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment;filename=custom_report_2024.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(result -> assertTrue(result.getResponse().getContentAsByteArray().length > 0))
                .andExpect(result -> {
                    byte[] responseBytes = result.getResponse().getContentAsByteArray();
                    String responseHeader = new String(Arrays.copyOf(responseBytes, 5));
                    assertEquals("%PDF-", responseHeader);
                });
    }
}
