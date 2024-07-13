package com.popov.fintrack.web.security.expression;

import com.popov.fintrack.budget.BudgetRepository;
import com.popov.fintrack.transaction.TransactionRepository;
import com.popov.fintrack.user.model.Role;
import com.popov.fintrack.wallet.WalletRepository;
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

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final BudgetRepository budgetRepository;

    public boolean hasAccessUser(final Long id) {
        Long userId = getAuthenticatedUserId();
        return userId.equals(id) || hasAnyRole(Role.ROLE_ADMIN);
    }

    public boolean hasAccessToWallets(final List<Long> walletIds) {
        return walletIds.stream().allMatch(this::hasAccessToWallet);
    }

    public boolean hasAccessToWallet(final Long walletId) {
        Long userId = getAuthenticatedUserId();
        boolean isOwner = walletRepository.existsByIdAndOwnerId(walletId, userId);
        boolean isMember = walletRepository.existsByUserIdAndId(userId, walletId);

        return isOwner || isMember;
    }

    public boolean hasAccessToBudget(final Long budgetId) {
        Long userId = getAuthenticatedUserId();
        return budgetRepository.existsByIdAndOwnerId(budgetId, userId);
    }

    public boolean hasAccessToTransaction(final Long transactionId) {
        Long userId = getAuthenticatedUserId();
        return transactionRepository.existsByIdAndOwnerId(transactionId, userId);
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
