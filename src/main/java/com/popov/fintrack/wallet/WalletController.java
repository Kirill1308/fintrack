package com.popov.fintrack.wallet;

import com.popov.fintrack.email.MailService;
import com.popov.fintrack.email.utils.MailType;
import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.user.dto.user.UserDTO;
import com.popov.fintrack.wallet.dto.InvitationRequest;
import com.popov.fintrack.wallet.dto.InvitationResponse;
import com.popov.fintrack.wallet.dto.WalletDTO;
import com.popov.fintrack.wallet.model.Invitation;
import com.popov.fintrack.wallet.model.InvitationStatus;
import com.popov.fintrack.wallet.model.Wallet;
import com.popov.fintrack.web.mapper.UserMapper;
import com.popov.fintrack.web.mapper.WalletMapper;
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
public class WalletController {

    private static final String INVITATION_LINK = "http://localhost:8080/api/v1/wallet/accept/";

    private final WalletService walletService;
    private final MailService mailService;
    private final InvitationService invitationService;

    private final WalletMapper walletMapper;
    private final UserMapper userMapper;

    @GetMapping("/{walletId}")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public WalletDTO getWalletById(@PathVariable Long walletId) {
        Wallet wallet = walletService.getWalletById(walletId);
        return walletMapper.toDto(wallet);
    }

    @PutMapping
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletDTO.id)")
    public WalletDTO updateWallet(@RequestBody WalletDTO walletDTO) {
        Wallet wallet = walletMapper.toEntity(walletDTO);
        Wallet updatedWallet = walletService.updateWallet(wallet);
        return walletMapper.toDto(updatedWallet);
    }

    @DeleteMapping("/{walletId}")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public void deleteWallet(@PathVariable Long walletId) {
        walletService.deleteWallet(walletId);
    }

    @GetMapping("/{walletId}/members")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public List<UserDTO> getWalletMembers(@PathVariable Long walletId) {
        List<Invitation> invitations = invitationService.findByWalletId(walletId);
        return invitations.stream()
                .map(Invitation::getRecipient)
                .map(userMapper::toDto)
                .toList();
    }

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

    @PostMapping("/accept/{token}")
    public String acceptInvitation(@PathVariable String token) {
        Invitation invitation = invitationService.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationService.updateInvitation(invitation);
        return "Invitation accepted";
    }

    @PostMapping("/{walletId}/exclude/{userId}")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public String excludeUserFromWallet(@PathVariable Long walletId, @PathVariable Long userId) {
        Invitation invitation = invitationService.findByWalletIdAndRecipientId(walletId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        Properties mailProperties = new Properties();
        mailProperties.put("walletName", invitation.getWallet().getName());

        invitationService.deleteInvitation(invitation);
        mailService.sendEmail(invitation.getRecipient(), MailType.EXCLUSION, mailProperties);

        return "User has been excluded from the wallet and notified";
    }
}
