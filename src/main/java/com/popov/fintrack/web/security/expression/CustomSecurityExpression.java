package com.popov.fintrack.web.security.expression;

import com.popov.fintrack.budget.BudgetService;
import com.popov.fintrack.transaction.TransactionService;
import com.popov.fintrack.user.model.Role;
import com.popov.fintrack.wallet.WalletService;
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

    public boolean hasAccessUser(final Long id) {
        Long userId = getAuthenticatedUserId();
        return userId.equals(id) || hasAnyRole(Role.ROLE_ADMIN);
    }

    public boolean hasAccessToWallet(Long walletId) {
        Long userId = getAuthenticatedUserId();
        boolean isOwner = walletService.isOwnerOfWallet(userId, walletId);
        boolean isMember = walletService.isMemberOfWallet(userId, walletId);
        boolean isAdmin = hasAnyRole(Role.ROLE_ADMIN);
        return isOwner || isMember || isAdmin;
    }

    public boolean hasAccessToWallets(List<Long> walletIds) {
        return walletIds.stream().allMatch(this::hasAccessToWallet);
    }

    public boolean hasAccessToBudget(Long budgetId) {
        Long userId = getAuthenticatedUserId();
        boolean isAdmin = hasAnyRole(Role.ROLE_ADMIN);
        boolean isOwner = budgetService.isOwnerOfBudget(userId, budgetId);
        return isAdmin || isOwner;
    }

    public boolean hasAccessToTransaction(final Long transactionId) {
        Long userId = getAuthenticatedUserId();
        boolean isAdmin = hasAnyRole(Role.ROLE_ADMIN);
        boolean isOwner = transactionService.isOwnerOfTransaction(userId, transactionId);
        return isAdmin || isOwner;
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

    private Long getAuthenticatedUserId() {
        JwtEntity user = getPrincipal();
        return user.getId();
    }

    private JwtEntity getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (JwtEntity) authentication.getPrincipal();
    }
}
