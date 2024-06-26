package com.popov.fintrack.wallet;

import com.popov.fintrack.AbstractControllerTest;
import com.popov.fintrack.JsonUtil;
import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.wallet.dto.WalletDTO;
import com.popov.fintrack.wallet.model.Wallet;
import com.popov.fintrack.web.mapper.WalletMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.popov.fintrack.user.UserTestData.USER_MAIL;
import static com.popov.fintrack.wallet.WalletTestData.WALLET_DTO_MATCHER;
import static com.popov.fintrack.wallet.WalletTestData.WALLET_ID;
import static com.popov.fintrack.wallet.WalletTestData.wallet;
import static com.popov.fintrack.wallet.WalletTestData.walletDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WalletControllerTest extends AbstractControllerTest {

    @MockBean
    private WalletService walletService;

    @MockBean
    private WalletMapper walletMapper;

    @Test
    @WithUserDetails(USER_MAIL)
    void getWalletById_returnsWalletDto() throws Exception {

        given(walletService.getWalletById(anyLong())).willReturn(wallet);
        given(walletMapper.toDto(wallet)).willReturn(walletDTO);

        mockMvc.perform(get("/api/v1/wallets/{walletId}", WALLET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(WALLET_DTO_MATCHER.contentJson(walletDTO));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void getWalletById_notFound() throws Exception {
        Mockito.when(walletService.getWalletById(anyLong())).thenThrow(new ResourceNotFoundException("Wallet not found"));

        mockMvc.perform(get("/api/v1/wallets/{walletId}", WALLET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void createWallet_returnsWalletDto() throws Exception {
        Mockito.when(walletService.createWallet(any(Wallet.class))).thenReturn(wallet);
        Mockito.when(walletMapper.toEntity(any(WalletDTO.class))).thenReturn(wallet);
        Mockito.when(walletMapper.toDto(any(Wallet.class))).thenReturn(walletDTO);

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(walletDTO)))
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
        Mockito.when(walletService.updateWallet(any(Wallet.class))).thenReturn(wallet);
        Mockito.when(walletMapper.toEntity(any(WalletDTO.class))).thenReturn(wallet);
        Mockito.when(walletMapper.toDto(any(Wallet.class))).thenReturn(walletDTO);

        mockMvc.perform(put("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(walletDTO)))
                .andExpect(status().isOk())
                .andExpect(WALLET_DTO_MATCHER.contentJson(walletDTO));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void deleteWallet_success() throws Exception {
        Mockito.doNothing().when(walletService).deleteWallet(anyLong());

        mockMvc.perform(delete("/api/v1/wallets/{walletId}", WALLET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void deleteWallet_notFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Wallet not found")).when(walletService).deleteWallet(anyLong());

        mockMvc.perform(delete("/api/v1/wallets/{walletId}", WALLET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
