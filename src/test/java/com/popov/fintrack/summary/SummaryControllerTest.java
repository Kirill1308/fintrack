package com.popov.fintrack.summary;

import com.popov.fintrack.AbstractControllerTest;
import com.popov.fintrack.validation.WithMockJwtUser;
import com.popov.fintrack.transaction.dto.FilterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.Month;
import java.util.List;

import static com.popov.fintrack.summary.SummaryTestData.CUSTOM_SUMMARY_MATCHER;
import static com.popov.fintrack.summary.SummaryTestData.FINANCIAL_SUMMARY_MATCHER;
import static com.popov.fintrack.summary.SummaryTestData.MONTHLY_SUMMARY_MATCHER;
import static com.popov.fintrack.summary.SummaryTestData.YEARLY_SUMMARY_MATCHER;
import static com.popov.fintrack.summary.SummaryTestData.customSummary;
import static com.popov.fintrack.summary.SummaryTestData.financialSummary;
import static com.popov.fintrack.summary.SummaryTestData.monthlySummary;
import static com.popov.fintrack.summary.SummaryTestData.yearlySummary;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SummaryControllerTest extends AbstractControllerTest {

    @MockBean
    private SummaryService summaryService;

    private FilterDTO filterDTO;

    @BeforeEach
    void setUp() {
        filterDTO = new FilterDTO();
        filterDTO.setWalletIds(List.of(1L));
    }

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void getFinancialSummary_success() throws Exception {
        given(summaryService.getFinancialSummary(any(FilterDTO.class))).willReturn(financialSummary);

        mockMvc.perform(post("/api/v1/summaries/summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterDTO)))
                .andExpect(status().isOk())
                .andExpect(FINANCIAL_SUMMARY_MATCHER.contentJson(financialSummary));
    }

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void getCustomSummary_success() throws Exception {
        given(summaryService.getCustomSummary(any(FilterDTO.class))).willReturn(customSummary);

        mockMvc.perform(post("/api/v1/summaries/custom-summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterDTO)))
                .andExpect(status().isOk())
                .andExpect(CUSTOM_SUMMARY_MATCHER.contentJson(customSummary));
    }

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void getYearlySummary_success() throws Exception {
        given(summaryService.getYearlySummary(anyInt(), anyLong())).willReturn(yearlySummary);

        mockMvc.perform(get("/api/v1/summaries/wallets/{walletId}/yearly-summary", 1L)
                        .param("year", "2023")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(YEARLY_SUMMARY_MATCHER.contentJson(yearlySummary));
    }

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void getMonthlySummary_success() throws Exception {
        given(summaryService.getMonthlySummary(anyInt(), any(Month.class), anyLong())).willReturn(monthlySummary);

        mockMvc.perform(get("/api/v1/summaries/wallets/{walletId}/monthly-summary", 1L)
                        .param("year", "2023")
                        .param("month", "JANUARY")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MONTHLY_SUMMARY_MATCHER.contentJson(monthlySummary));
    }
}
