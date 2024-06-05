package com.popov.fintrack.web.security.utils;

import com.popov.fintrack.user.UserRepository;
import com.popov.fintrack.user.model.User;
import com.popov.fintrack.web.security.JwtEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    public static JwtEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtEntity) {
            return (JwtEntity) authentication.getPrincipal();
        } else {
            throw new IllegalStateException("No authenticated JwtEntity found in SecurityContext");
        }
    }

    public static Long getAuthenticatedUserId() {
        return getAuthenticatedUser().getId();
    }

    public static User getCurrentUser(UserRepository userRepository) {
        return userRepository.findById(getAuthenticatedUserId())
                .orElseThrow(() -> new IllegalStateException("No user found for id " + getAuthenticatedUserId()));
    }
}
