package com.popov.fintrack.user;

import com.popov.fintrack.AbstractControllerTest;
import com.popov.fintrack.budget.dto.BudgetDTO;
import com.popov.fintrack.wallet.dto.WalletDTO;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

import static com.popov.fintrack.budget.BudgetTestData.budgetDTO;
import static com.popov.fintrack.user.UserTestData.ADMIN_MAIL;
import static com.popov.fintrack.user.UserTestData.USER1_ID;
import static com.popov.fintrack.user.UserTestData.USER2_ID;
import static com.popov.fintrack.user.UserTestData.USER_MAIL;
import static com.popov.fintrack.user.UserTestData.userDTO;
import static com.popov.fintrack.wallet.WalletTestData.walletDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(ADMIN_MAIL)
    @Description("Admin can get information about any user")
    void getUserById_adminAuth_returnsUserDto() throws Exception {
        mockMvc.perform(get("/api/v1/users/{id}", USER1_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("User can get information about himself")
    void getUserById_userAuth_returnsUserDto() throws Exception {
        mockMvc.perform(get("/api/v1/users/{id}", USER1_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("User cannot get information about another user")
    void getUserById_userAuth_forbidden() throws Exception {
        mockMvc.perform(get("/api/v1/users/{id}", USER2_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    @Description("Admin can delete any user")
    void deleteUserById_success() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", USER1_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("User cannot delete another user")
    void deleteUserById_forbidden() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", USER2_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("User can get wallets of himself")
    void getWallets_userAuth_returnsWalletDtos() throws Exception {
        List<WalletDTO> walletDTOs = List.of(walletDTO);

        mockMvc.perform(get("/api/v1/users/{userId}/wallets", USER1_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(walletDTOs.get(0).getId()));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("User cannot get wallets of another user")
    void getWallets_userAuth_cannotGetWalletsOfAnotherUser() throws Exception {
        mockMvc.perform(get("/api/v1/users/{userId}/wallets", USER2_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("User can get budgets of himself")
    void getBudgets_userAuth_returnsBudgetDtos() throws Exception {
        List<BudgetDTO> budgetDTOs = List.of(budgetDTO);

        mockMvc.perform(get("/api/v1/users/{userId}/budgets", USER1_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(budgetDTOs.get(0).getId()));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("User cannot get budgets of another user")
    void getBudgets_userAuth_cannotGetBudgetsOfAnotherUser() throws Exception {
        mockMvc.perform(get("/api/v1/users/{userId}/budgets", USER2_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
