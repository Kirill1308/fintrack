package com.popov.fintrack.wallet;

import com.popov.fintrack.AbstractControllerTest;
import com.popov.fintrack.JsonUtil;
import com.popov.fintrack.wallet.dto.WalletDTO;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.popov.fintrack.user.UserTestData.USER_MAIL;
import static com.popov.fintrack.user.UserTestData.WALLET_MEMBER_MAIL;
import static com.popov.fintrack.wallet.WalletTestData.NOT_EXISTING_WALLET_ID;
import static com.popov.fintrack.wallet.WalletTestData.SHARED_WALLET_ID;
import static com.popov.fintrack.wallet.WalletTestData.WALLET_DTO_MATCHER;
import static com.popov.fintrack.wallet.WalletTestData.WALLET_ID;
import static com.popov.fintrack.wallet.WalletTestData.walletDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WalletControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(USER_MAIL)
    @Description("Should return wallet by id for wallet owner")
    void getWalletById_forWalletOwner_returnsWalletDto() throws Exception {
        mockMvc.perform(get("/api/v1/wallets/{walletId}", WALLET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(WALLET_DTO_MATCHER.contentJson(walletDTO));
    }

    @Test
    @WithUserDetails(WALLET_MEMBER_MAIL)
    @Description("Should return wallet by id for shared wallet")
    void getWalletById_forSharedWallet_returnsWalletDto() throws Exception {
        mockMvc.perform(get("/api/v1/wallets/{walletId}", SHARED_WALLET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(WALLET_DTO_MATCHER.contentJson(walletDTO));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void getWalletById_accessDenied() throws Exception {
        mockMvc.perform(get("/api/v1/wallets/{walletId}", NOT_EXISTING_WALLET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void createWallet_returnsWalletDto() throws Exception {
        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletDTO)))
                .andExpect(status().isOk())
                .andExpect(WALLET_DTO_MATCHER.contentJson(walletDTO));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void createWallet_invalidInput() throws Exception {
        WalletDTO invalidWalletDTO = new WalletDTO();

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalidWalletDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void updateWallet_returnsWalletDto() throws Exception {
        mockMvc.perform(put("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(walletDTO)))
                .andExpect(status().isOk())
                .andExpect(WALLET_DTO_MATCHER.contentJson(walletDTO));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void deleteWallet_success() throws Exception {
        mockMvc.perform(delete("/api/v1/wallets/{walletId}", WALLET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void deleteWallet_accessDenied() throws Exception {
        mockMvc.perform(delete("/api/v1/wallets/{walletId}", NOT_EXISTING_WALLET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
