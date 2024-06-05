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
        return walletService.isMemberOfWallet(userId, walletId);
    }

    public boolean hasAccessToWallets(List<Long> walletIds) {
        return walletIds.stream().allMatch(this::hasAccessToWallet);
    }

    public boolean hasAccessToBudget(Long budgetId) {
        Long userId = getAuthenticatedUserId();
        return budgetService.isMemberOfBudget(userId, budgetId);
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
