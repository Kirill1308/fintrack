package com.popov.fintrack.budget;

import com.popov.fintrack.AbstractControllerTest;
import com.popov.fintrack.budget.dto.BudgetDTO;
import com.popov.fintrack.budget.model.Budget;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.web.mapper.BudgetMapper;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

import static com.popov.fintrack.budget.BudgetTestData.ANOTHER_USERS_BUDGET_ID;
import static com.popov.fintrack.budget.BudgetTestData.BUDGET_DTO_MATCHER;
import static com.popov.fintrack.budget.BudgetTestData.OWNED_BUDGET_ID;
import static com.popov.fintrack.budget.BudgetTestData.budget;
import static com.popov.fintrack.budget.BudgetTestData.budgetDTO;
import static com.popov.fintrack.user.UserTestData.USER_MAIL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
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
    @WithUserDetails(USER_MAIL)
    @Description("User can get only his budget")
    void getBudgetById_returnsBudgetDto() throws Exception {
        mockMvc.perform(get("/api/v1/budgets/{budgetId}", OWNED_BUDGET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(BUDGET_DTO_MATCHER.contentJson(budgetDTO));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("User cannot get another user's budget")
    void getBudgetById_returnsForbidden() throws Exception {
        given(budgetService.getBudgetById(anyLong())).willReturn(budget);
        given(budgetMapper.toDto(any(Budget.class))).willReturn(budgetDTO);

        mockMvc.perform(get("/api/v1/budgets/{budgetId}", ANOTHER_USERS_BUDGET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void createBudget_returnsBudgetDto() throws Exception {
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
    @WithUserDetails(USER_MAIL)
    void updateBudget_returnsBudgetDto() throws Exception {
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
    @WithUserDetails(USER_MAIL)
    @Description("User can delete his budget")
    void deleteBudget_userAuth_success() throws Exception {
        mockMvc.perform(delete("/api/v1/budgets/{budgetId}", OWNED_BUDGET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("User cannot delete another user's budget")
    void deleteBudget_userAuth_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/api/v1/budgets/{budgetId}", ANOTHER_USERS_BUDGET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
