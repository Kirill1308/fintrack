package com.popov.fintrack.validation;

import com.popov.fintrack.web.security.JwtEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.stream.Stream;

@Slf4j
public class WithMockJwtUserSecurityContextFactory implements WithSecurityContextFactory<WithMockJwtUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwtUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        JwtEntity principal = new JwtEntity(
                customUser.id(),
                customUser.username(),
                customUser.name(),
                "password",
                Stream.of(customUser.roles())
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .toList()
        );

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);

        System.out.println("Created security context with principal: " + principal.getId() + " " + principal.getUsername() + " " + principal.getName() + " " + principal.getAuthorities());
        return context;
    }
}

