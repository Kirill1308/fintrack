package com.popov.fintrack.wallet.membership;

import com.popov.fintrack.event.ExclusionEvent;
import com.popov.fintrack.event.InvitationConfirmEvent;
import com.popov.fintrack.exception.DataConflictException;
import com.popov.fintrack.user.UserService;
import com.popov.fintrack.user.dto.user.UserDTO;
import com.popov.fintrack.user.model.User;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.dto.InvitationRequest;
import com.popov.fintrack.wallet.dto.InvitationResponse;
import com.popov.fintrack.wallet.membership.model.Member;
import com.popov.fintrack.wallet.model.ConfirmData;
import com.popov.fintrack.wallet.model.Wallet;
import com.popov.fintrack.web.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/wallets/membership")
@RequiredArgsConstructor
@Tag(name = "Wallet Membership Controller", description = "API related to wallet membership")
public class WalletMembershipController {

    private final ApplicationEventPublisher eventPublisher;
    private final MemberService memberService;
    private final WalletService walletService;
    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Get all members of a wallet")
    @ApiResponses(value = {
            @ApiResponse(description = "List of members", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/members")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public List<UserDTO> getWalletMembers(@RequestParam Long walletId) {
        log.info("Fetching members of wallet with ID: {}", walletId);
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
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#invitation.walletId)")
    public InvitationResponse sendInvitation(@RequestBody InvitationRequest invitation, HttpServletRequest req) {
        log.info("Sending invitation to email: {} for wallet ID: {}", invitation.getRecipientEmail(), invitation.getWalletId());
        ConfirmData confirmData = new ConfirmData(invitation);
        req.getSession().setAttribute("token", confirmData);

        eventPublisher.publishEvent(new InvitationConfirmEvent(invitation, confirmData.getToken()));

        InvitationResponse response = new InvitationResponse();
        response.setAcceptToken(confirmData.getToken());

        log.info("Invitation sent with token: {}", confirmData.getToken());
        return response;
    }

    @Operation(summary = "Accept an invitation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invitation accepted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/accept")
    public String acceptInvitation(@RequestParam String token, HttpSession session) {
        log.info("Accepting invitation with token: {}", token);
        ConfirmData confirmData = (ConfirmData) session.getAttribute("token");

        if (token.equals(confirmData.getToken())) {
            User recipient = userService.getByUsername(confirmData.getInvitation().getRecipientEmail());
            Wallet wallet = walletService.getWalletById(confirmData.getInvitation().getWalletId());
            memberService.addMember(wallet, recipient);
            session.invalidate();
            log.info("Invitation accepted for wallet ID: {} and user: {}", confirmData.getInvitation().getWalletId(), recipient.getUsername());
            return "Invitation accepted";
        }
        log.error("Token mismatch error for token: {}", token);
        throw new DataConflictException("Token mismatch error");
    }

    @Operation(summary = "Exclude a user from a wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User excluded from wallet and notified", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/exclude")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    public String excludeUserFromWallet(@RequestParam Long walletId, @RequestParam Long userId) {
        log.info("Excluding user with ID: {} from wallet with ID: {}", userId, walletId);
        User recipient = userService.getUserById(userId);
        Wallet wallet = walletService.getWalletById(walletId);
        memberService.excludeMember(wallet, recipient);

        eventPublisher.publishEvent(new ExclusionEvent(userId, walletId));

        log.info("User with ID: {} excluded from wallet with ID: {}", userId, walletId);
        return "User has been excluded from the wallet and notified";
    }
}
