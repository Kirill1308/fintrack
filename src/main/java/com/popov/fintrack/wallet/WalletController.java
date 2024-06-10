package com.popov.fintrack.wallet;

import com.popov.fintrack.email.MailService;
import com.popov.fintrack.email.utils.MailType;
import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.user.dto.user.UserDTO;
import com.popov.fintrack.user.model.member.Member;
import com.popov.fintrack.wallet.dto.InvitationRequest;
import com.popov.fintrack.wallet.dto.InvitationResponse;
import com.popov.fintrack.wallet.dto.WalletDTO;
import com.popov.fintrack.wallet.model.Invitation;
import com.popov.fintrack.wallet.model.InvitationStatus;
import com.popov.fintrack.wallet.model.Wallet;
import com.popov.fintrack.web.mapper.UserMapper;
import com.popov.fintrack.web.mapper.WalletMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
@Tag(name = "Wallet Controller", description = "API related to wallets")
public class WalletController {

    private static final String INVITATION_LINK = "http://localhost:8080/api/v1/wallet/accept/";

    private final WalletService walletService;
    private final MailService mailService;
    private final InvitationService invitationService;

    private final WalletMapper walletMapper;
    private final UserMapper userMapper;

    @Operation(summary = "Get wallet by ID")
    @ApiResponses(value = {
            @ApiResponse(description = "Get Wallet Details", responseCode = "200",
                    content = @Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = WalletDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{walletId}")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public WalletDTO getWalletById(@PathVariable Long walletId) {
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
    public WalletDTO createWallet(@RequestBody WalletDTO walletDTO) {
        Wallet wallet = walletMapper.toEntity(walletDTO);
        Wallet updatedWallet = walletService.createWallet(wallet);
        return walletMapper.toDto(updatedWallet);
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
    public WalletDTO updateWallet(@RequestBody WalletDTO walletDTO) {
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
        walletService.deleteWallet(walletId);
    }

    @Operation(summary = "Get all members of a wallet")
    @ApiResponses(value = {
            @ApiResponse(description = "List of members", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{walletId}/members")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public List<UserDTO> getWalletMembers(@PathVariable Long walletId) {
        List<Member> members = walletService.findAllMembers(walletId);
        return members.stream()
                .map(Member::getUser)
                .map(userMapper::toDto)
                .toList();
    }

    @Operation(summary = "Send an invitation to join a wallet")
    @ApiResponses(value = {
            @ApiResponse(description = "Invitation was sent", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = InvitationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/invite")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#request.walletId)")
    public InvitationResponse sendInvitation(@RequestBody InvitationRequest request) {
        Invitation invitation = invitationService.saveInvitation(request);

        Properties mailProperties = new Properties();
        mailProperties.put("sender", invitation.getSender().getName());
        mailProperties.put("invitationLink", INVITATION_LINK + invitation.getToken());

        mailService.sendEmail(invitation.getRecipient(), MailType.INVITATION, mailProperties);

        InvitationResponse response = new InvitationResponse();
        response.setAcceptToken(invitation.getToken());

        return response;
    }

    @Operation(summary = "Accept an invitation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invitation accepted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/accept/{token}")
    public String acceptInvitation(@PathVariable String token) {
        Invitation invitation = invitationService.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        invitation.setStatus(InvitationStatus.ACCEPTED);

        invitationService.updateInvitation(invitation);
        return "Invitation accepted";
    }

    @Operation(summary = "Exclude a user from a wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User excluded from wallet and notified", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/{walletId}/exclude/{userId}")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public String excludeUserFromWallet(@PathVariable Long walletId, @PathVariable Long userId) {
        Invitation invitation = invitationService.findInvitation(walletId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        Properties mailProperties = new Properties();
        mailProperties.put("walletName", invitation.getWallet().getName());

        invitationService.deleteInvitation(invitation);
        mailService.sendEmail(invitation.getRecipient(), MailType.EXCLUSION, mailProperties);

        return "User has been excluded from the wallet and notified";
    }
}
