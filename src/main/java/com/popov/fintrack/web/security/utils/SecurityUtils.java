package com.popov.fintrack.web.security.utils;

import com.popov.fintrack.web.security.JwtEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {

    public static JwtEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtEntity jwtEntity) {
            return jwtEntity;
        } else {
            throw new IllegalStateException("No authenticated JwtEntity found in SecurityContext");
        }
    }

    public static Long getAuthenticatedUserId() {
        return getAuthenticatedUser().getId();
    }
}
