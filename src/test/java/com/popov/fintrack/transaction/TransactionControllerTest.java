package com.popov.fintrack.transaction;

import com.popov.fintrack.AbstractControllerTest;
import com.popov.fintrack.transaction.dto.AmountRange;
import com.popov.fintrack.transaction.dto.DateRange;
import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.transaction.model.Category;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static com.popov.fintrack.transaction.TransactionTestData.TRANSACTION_DTO_MATCHER;
import static com.popov.fintrack.transaction.TransactionTestData.USER_1_TRANSACTION_ID;
import static com.popov.fintrack.transaction.TransactionTestData.transactionDTO;
import static com.popov.fintrack.user.UserTestData.USER1_ID;
import static com.popov.fintrack.user.UserTestData.USER_MAIL;
import static com.popov.fintrack.wallet.WalletTestData.USER_1_WALLET_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransactionControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("User can get transactions by user id")
    void getFilteredTransactions_byUserId() throws Exception {
        FilterDTO filterDTO = new FilterDTO();
        filterDTO.setUserIds(List.of(USER1_ID));

        performFilterRequest(filterDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(transactionDTO.getId()));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("User can get transactions by wallet id")
    void getFilteredTransactions_byWalletId() throws Exception {
        FilterDTO filterDTO = new FilterDTO();
        filterDTO.setWalletIds(List.of(USER_1_WALLET_ID));

        performFilterRequest(filterDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(transactionDTO.getId()));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("User can get transactions by category")
    void getFilteredTransactions_byCategory() throws Exception {
        FilterDTO filterDTO = new FilterDTO();
        filterDTO.setCategories(List.of(Category.GROCERIES));

        performFilterRequest(filterDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].category").value("GROCERIES"));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("User can get transactions by date range")
    void getFilteredTransactions_byDateRange() throws Exception {
        FilterDTO filterDTO = new FilterDTO();
        DateRange dateRange = DateRange.builder()
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 6, 1))
                .build();
        filterDTO.setDateRange(dateRange);

        performFilterRequest(filterDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].dateCreated").exists());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("User can get transactions by amount range")
    void getFilteredTransactions_byAmountRange() throws Exception {
        FilterDTO filterDTO = new FilterDTO();
        AmountRange amountRange = new AmountRange();
        amountRange.setMin(50.0);
        amountRange.setMax(150.0);
        filterDTO.setAmountRange(amountRange);

        performFilterRequest(filterDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].amount").value(50.0));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("User can get transactions by note")
    void getFilteredTransactions_byNote() throws Exception {
        FilterDTO filterDTO = new FilterDTO();
        filterDTO.setNote("Grocery shopping");

        performFilterRequest(filterDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].note").value("Grocery shopping"));
    }

    private ResultActions performFilterRequest(FilterDTO filterDTO) throws Exception {
        return mockMvc.perform(post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("limit", "10")
                .content(objectMapper.writeValueAsString(filterDTO)));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("User can access his transactions")
    void getTransactionById_returnsTransactionDto() throws Exception {
        mockMvc.perform(get("/api/v1/transactions/{transactionId}", USER_1_TRANSACTION_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transactionDTO.getId()));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void createTransaction_success() throws Exception {
        mockMvc.perform(post("/api/v1/transactions/{walletId}", USER_1_WALLET_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk())
                .andExpect(TRANSACTION_DTO_MATCHER.contentJson(transactionDTO));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void deleteTransaction_success() throws Exception {
        mockMvc.perform(delete("/api/v1/transactions/{transactionId}", USER_1_TRANSACTION_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
