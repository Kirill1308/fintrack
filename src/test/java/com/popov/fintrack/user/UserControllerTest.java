package com.popov.fintrack.user;

import com.popov.fintrack.AbstractControllerTest;
import com.popov.fintrack.budget.BudgetService;
import com.popov.fintrack.user.model.User;
import com.popov.fintrack.validation.WithMockJwtUser;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.web.mapper.BudgetMapper;
import com.popov.fintrack.web.mapper.WalletMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;

import static com.popov.fintrack.budget.BudgetTestData.budget;
import static com.popov.fintrack.budget.BudgetTestData.budgetDTO;
import static com.popov.fintrack.user.UserTestData.USER_ID;
import static com.popov.fintrack.user.UserTestData.user;
import static com.popov.fintrack.user.UserTestData.userDTO;
import static com.popov.fintrack.wallet.WalletTestData.wallet;
import static com.popov.fintrack.wallet.WalletTestData.walletDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserControllerTest extends AbstractControllerTest {

    @Mock
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private WalletService walletService;

    @MockBean
    private WalletMapper walletMapper;

    @MockBean
    private BudgetService budgetService;

    @MockBean
    private BudgetMapper budgetMapper;

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void getUserById_success() throws Exception {
        mockMvc.perform(get("/api/v1/users/{id}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void deleteUserById_success() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void getWallets_success() throws Exception {
        given(walletService.getWallets(anyLong())).willReturn(List.of(wallet));
        given(walletMapper.toDto(any(List.class))).willReturn(List.of(walletDTO));

        mockMvc.perform(get("/api/v1/users/{userId}/wallets", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(walletDTO.getId()));
    }

    @Test
    @WithMockJwtUser(username = "john.doe@example.com", roles = {"USER", "ADMIN"})
    void getBudgetsByUserId_success() throws Exception {
        given(budgetService.getBudgets(anyLong())).willReturn(List.of(budget));
        given(budgetMapper.toDto(any(List.class))).willReturn(List.of(budgetDTO));

        mockMvc.perform(get("/api/v1/users/{userId}/budgets", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(budgetDTO.getId()));
    }
}
