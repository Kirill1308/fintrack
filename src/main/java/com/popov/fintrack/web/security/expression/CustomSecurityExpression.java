package com.popov.fintrack.web.security.expression;

import com.popov.fintrack.budget.BudgetService;
import com.popov.fintrack.transaction.TransactionService;
import com.popov.fintrack.transaction.model.Transaction;
import com.popov.fintrack.user.model.Role;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.web.security.JwtEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("customSecurityExpression")
@RequiredArgsConstructor
@Slf4j
public class CustomSecurityExpression {

    private final WalletService walletService;
    private final BudgetService budgetService;
    private final TransactionService transactionService;

    public boolean hasAccessUser(final Long id) {
        Long userId = getAuthenticatedUserId();
        return userId.equals(id) || hasAnyRole(Role.ROLE_ADMIN);
    }

    public boolean hasAccessToWallets(final List<Long> walletIds) {
        return walletIds.stream().allMatch(this::hasAccessToWallet);
    }

    public boolean hasAccessToWallet(final Long walletId) {
        Long userId = getAuthenticatedUserId();
        boolean isOwner = walletService.isOwnerOfWallet(userId, walletId);
        boolean isMember = walletService.isMemberOfWallet(userId, walletId);
        return isOwner || isMember;
    }

    public boolean hasAccessToBudget(final Long budgetId) {
        Long userId = getAuthenticatedUserId();
        return budgetService.isOwnerOfBudget(userId, budgetId);
    }

    public boolean hasAccessToTransaction(final Long transactionId) {
        Long userId = getAuthenticatedUserId();
        System.out.println("userId: " + userId + " transactionId: " + transactionId);

        boolean isOwner = transactionService.isOwnerOfTransaction(transactionId, userId);
        System.out.println("isOwner: " + isOwner);

        return isOwner;
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
