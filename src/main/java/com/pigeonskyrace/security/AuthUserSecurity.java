package com.pigeonskyrace.security;

import org.springframework.stereotype.Component;

/**
 * SpEL helper for {@link org.springframework.security.access.prepost.PreAuthorize} on user-scoped routes.
 */
@Component("authUserSecurity")
public class AuthUserSecurity {

    public boolean isSelf(String userId) {
        if (userId == null) {
            return false;
        }
        String trimmed = userId.trim();
        return SecurityUtils.currentUser()
                .map(UserPrincipal::getId)
                .map(id -> id.equals(trimmed))
                .orElse(false);
    }
}
