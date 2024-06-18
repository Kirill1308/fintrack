package com.popov.fintrack.budget;

import com.popov.fintrack.AbstractControllerTest;
import com.popov.fintrack.validation.WithMockJwtUser;
import com.popov.fintrack.budget.dto.BudgetDTO;
import com.popov.fintrack.budget.model.Budget;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.web.mapper.BudgetMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;

import static com.popov.fintrack.budget.BudgetTestData.BUDGET_DTO_MATCHER;
import static com.popov.fintrack.budget.BudgetTestData.budget;
import static com.popov.fintrack.budget.BudgetTestData.budgetDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BudgetControllerTest extends AbstractControllerTest {

    @MockBean
    private BudgetService budgetService;

    @MockBean
    private WalletService walletService;

    @MockBean
    private BudgetMapper budgetMapper;

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void getBudgetById_success() throws Exception {
        given(budgetService.getBudgetById(anyLong())).willReturn(budget);
        given(budgetMapper.toDto(any(Budget.class))).willReturn(budgetDTO);

        mockMvc.perform(get("/api/v1/budgets/{budgetId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(BUDGET_DTO_MATCHER.contentJson(budgetDTO));
    }

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void createBudget_success() throws Exception {
        given(budgetMapper.toEntity(any(BudgetDTO.class))).willReturn(budget);
        given(walletService.getWalletsByIds(any())).willReturn(List.of());
        given(budgetService.updateBudget(any(Budget.class))).willReturn(budget);
        given(budgetMapper.toDto(any(Budget.class))).willReturn(budgetDTO);

        mockMvc.perform(post("/api/v1/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetDTO)))
                .andExpect(status().isOk())
                .andExpect(BUDGET_DTO_MATCHER.contentJson(budgetDTO));
    }

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void updateBudget_success() throws Exception {
        given(budgetMapper.toEntity(any(BudgetDTO.class))).willReturn(budget);
        given(budgetService.updateBudget(any(Budget.class))).willReturn(budget);
        given(budgetMapper.toDto(any(Budget.class))).willReturn(budgetDTO);

        mockMvc.perform(put("/api/v1/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetDTO)))
                .andExpect(status().isOk())
                .andExpect(BUDGET_DTO_MATCHER.contentJson(budgetDTO));
    }

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void deleteBudget_success() throws Exception {
        doNothing().when(budgetService).deleteBudget(anyLong());

        mockMvc.perform(delete("/api/v1/budgets/{budgetId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
