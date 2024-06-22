package com.popov.fintrack.validation;

import com.popov.fintrack.web.security.JwtEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockJwtUserSecurityContextFactory implements WithSecurityContextFactory<WithMockJwtUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockJwtUser withMockJwtUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        System.out.println("Username: " + withMockJwtUser.username());
        System.out.println("Name: " + withMockJwtUser.name());
        System.out.println("Password: " + withMockJwtUser.password());

        JwtEntity principal = new JwtEntity(
                2L,
                withMockJwtUser.username(),
                withMockJwtUser.name(),
                withMockJwtUser.password(),
                AuthorityUtils.createAuthorityList(withMockJwtUser.roles())
        );

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal, principal.getPassword(), principal.getAuthorities());

        context.setAuthentication(authentication);
        return context;
    }
}
