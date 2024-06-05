package com.popov.fintrack.web.security.expression;

import com.popov.fintrack.budget.BudgetService;
import com.popov.fintrack.transaction.TransactionService;
import com.popov.fintrack.user.model.Role;
import com.popov.fintrack.wallet.InvitationService;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.model.Invitation;
import com.popov.fintrack.web.security.JwtEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final WalletService walletService;
    private final BudgetService budgetService;
    private final TransactionService transactionService;
    private final InvitationService invitationService;

    public boolean hasAccessUser(final Long id) {
        JwtEntity user = getPrincipal();
        Long userId = user.getId();

        return userId.equals(id) || hasAnyRole(Role.ROLE_ADMIN);
    }

    public boolean hasAccessToWallet(Long walletId) {
        JwtEntity user = getPrincipal();
        Long userId = user.getId();

        boolean isOwner = walletService.isOwnerOfWallet(userId, walletId);
        List<Invitation> invitations = invitationService.getAcceptedInvitations(userId);
        boolean hasBeenInvited = invitations.stream().anyMatch(invitation -> invitation.getWallet().getId().equals(walletId));
        return isOwner || hasBeenInvited;
    }

    public boolean hasAccessToWallets(List<Long> walletIds) {
        JwtEntity user = getPrincipal();
        Long userId = user.getId();

        for (Long walletId : walletIds) {
            boolean isOwner = walletService.isOwnerOfWallet(userId, walletId);
            List<Invitation> invitations = invitationService.getAcceptedInvitations(userId);
            boolean hasBeenInvited = invitations.stream().anyMatch(invitation -> invitation.getWallet().getId().equals(walletId));
            if (!(isOwner || hasBeenInvited)) {
                return false;
            }
        }

        return true;
    }

    public boolean hasAccessToBudget(Long budgetId) {
        JwtEntity user = getPrincipal();
        Long userId = user.getId();

        boolean isOwner = budgetService.isOwnerOfBudget(userId, budgetId);
        List<Invitation> invitations = invitationService.getAcceptedInvitations(userId);
        boolean hasBeenInvited = invitations.stream()
                .flatMap(invitation -> invitation.getWallet().getBudgets().stream())
                .anyMatch(budget -> budget.getId().equals(budgetId));

        return isOwner || hasBeenInvited;
    }

    private boolean hasAnyRole(final Role... roles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (Role role : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOwnerOfTransaction(final Long transactionId) {
        Long userId = getAuthenticatedUserId();
        return transactionService.isOwnerOfTransaction(userId, transactionId);
    }

    private Long getAuthenticatedUserId() {
        JwtEntity user = getPrincipal();
        return user.getId();
    }

    private JwtEntity getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (JwtEntity) authentication.getPrincipal();
    }
}
