package com.popov.fintrack.wallet;

import com.popov.fintrack.utills.validation.OnUpdate;
import com.popov.fintrack.wallet.dto.WalletDTO;
import com.popov.fintrack.wallet.model.Wallet;
import com.popov.fintrack.web.mapper.WalletMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
@Tag(name = "Wallet Controller", description = "API related to wallets")
public class WalletController {

    private final WalletService walletService;
    private final WalletMapper walletMapper;

    @Operation(summary = "Get wallet by ID")
    @ApiResponses(value = {
            @ApiResponse(description = "Get Wallet Details", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = WalletDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{walletId}")
//    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public WalletDTO getWalletById(@PathVariable Long walletId) {
        log.info("Get wallet by ID: {}", walletId);
        Wallet wallet = walletService.getWalletById(walletId);
        return walletMapper.toDto(wallet);
    }

    @Operation(summary = "Create a new wallet")
    @ApiResponses(value = {
            @ApiResponse(description = "Wallet created successfully", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = WalletDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public WalletDTO createWallet(@Validated @RequestBody WalletDTO walletDTO) {
        log.info("Creating wallet with details: {}", walletDTO);
        Wallet wallet = walletMapper.toEntity(walletDTO);
        Wallet createdWallet = walletService.createWallet(wallet);
        return walletMapper.toDto(createdWallet);
    }

    @Operation(summary = "Update an existing wallet")
    @ApiResponses(value = {
            @ApiResponse(description = "Wallet updated successfully", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = WalletDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletDTO.id)")
    public WalletDTO updateWallet(@Validated(OnUpdate.class) @RequestBody WalletDTO walletDTO) {
        log.info("Updating wallet with ID: {}", walletDTO.getId());
        Wallet wallet = walletMapper.toEntity(walletDTO);
        Wallet updatedWallet = walletService.updateWallet(wallet);
        return walletMapper.toDto(updatedWallet);
    }

    @Operation(summary = "Delete a wallet")
    @ApiResponses(value = {
            @ApiResponse(description = "Wallet deleted successfully", responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{walletId}")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public void deleteWallet(@PathVariable Long walletId) {
        log.info("Deleting wallet with ID: {}", walletId);
        walletService.deleteWallet(walletId);
        log.info("Wallet deleted with ID: {}", walletId);
    }
}
