package com.maleyk.api_gateway.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.addRequestHeader;

public class UserLoginHeaderFilter {

    public static HandlerFilterFunction<ServerResponse, ServerResponse> addUserLoginHeader() {
        return (request, next) -> {
            String login = extractLogin(SecurityContextHolder.getContext().getAuthentication());
            return login != null
                    ? addRequestHeader("X-User-Login", login).filter(request, next)
                    : next.handle(request);
        };
    }

    private static String extractLogin(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt jwt) {
            return jwt.getClaimAsString("preferred_username");
        }
        if (principal instanceof OidcUser oidcUser) {
            return oidcUser.getPreferredUsername();
        }
        return null;
    }
}