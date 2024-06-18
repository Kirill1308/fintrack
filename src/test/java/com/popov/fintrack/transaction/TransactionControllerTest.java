package com.popov.fintrack.transaction;

import com.popov.fintrack.AbstractControllerTest;
import com.popov.fintrack.validation.WithMockJwtUser;
import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.transaction.dto.TransactionDTO;
import com.popov.fintrack.transaction.model.Transaction;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.web.mapper.TransactionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;

import java.util.List;

import static com.popov.fintrack.transaction.TransactionTestData.TRANSACTION_DTO_MATCHER;
import static com.popov.fintrack.transaction.TransactionTestData.TRANSACTION_ID;
import static com.popov.fintrack.transaction.TransactionTestData.transaction;
import static com.popov.fintrack.transaction.TransactionTestData.transactionDTO;
import static com.popov.fintrack.wallet.WalletTestData.wallet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransactionControllerTest extends AbstractControllerTest {

    @MockBean
    private WalletService walletService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private TransactionMapper transactionMapper;

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void getFilteredTransactions_success() throws Exception {
        FilterDTO filterDTO = new FilterDTO();
        filterDTO.setWalletIds(List.of(1L));

        Page<Transaction> transactionPage = new PageImpl<>(List.of(transaction), PageRequest.of(0, 10), 1);
        given(transactionService.getFilteredTransactions(any(FilterDTO.class), any(PageRequest.class))).willReturn(transactionPage);
        given(transactionMapper.toDto(any(List.class))).willReturn(List.of(transactionDTO));

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("limit", "10")
                        .content(objectMapper.writeValueAsString(filterDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(transactionDTO.getId()))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void getTransactionById_success() throws Exception {
        given(transactionService.getTransaction(anyLong())).willReturn(transaction);
        given(transactionMapper.toDto(any(Transaction.class))).willReturn(transactionDTO);

        mockMvc.perform(get("/api/v1/transactions/{transactionId}", TRANSACTION_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(TRANSACTION_DTO_MATCHER.contentJson(transactionDTO));
    }

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void createTransaction_success() throws Exception {
        given(walletService.getWalletById(anyLong())).willReturn(wallet);
        given(transactionMapper.toEntity(any(TransactionDTO.class))).willReturn(transaction);
        given(transactionService.createTransaction(any(Transaction.class))).willReturn(transaction);
        given(transactionMapper.toDto(any(Transaction.class))).willReturn(transactionDTO);

        mockMvc.perform(post("/api/v1/transactions/{walletId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk())
                .andExpect(TRANSACTION_DTO_MATCHER.contentJson(transactionDTO));
    }

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void updateTransaction_success() throws Exception {
        given(transactionMapper.toEntity(any(TransactionDTO.class))).willReturn(transaction);
        given(transactionService.updateTransaction(any(Transaction.class))).willReturn(transaction);
        given(transactionMapper.toDto(any(Transaction.class))).willReturn(transactionDTO);

        mockMvc.perform(put("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk())
                .andExpect(TRANSACTION_DTO_MATCHER.contentJson(transactionDTO));
    }

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void deleteTransaction_success() throws Exception {
        doNothing().when(transactionService).deleteTransaction(anyLong());

        mockMvc.perform(delete("/api/v1/transactions/{transactionId}", TRANSACTION_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
