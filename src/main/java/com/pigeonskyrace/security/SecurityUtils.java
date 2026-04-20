package com.pigeonskyrace.security;

import com.pigeonskyrace.model.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<UserPrincipal> currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
            return Optional.empty();
        }
        return Optional.of(principal);
    }

    public static String requireUserIdHex() {
        return currentUser()
                .map(UserPrincipal::getId)
                .orElseThrow(() -> new IllegalStateException("Not authenticated"));
    }

    public static boolean hasRole(Role role) {
        String authority = role.getAuthority();
        Optional<UserPrincipal> u = currentUser();
        if (u.isEmpty()) {
            return false;
        }
        return u.get().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority::equals);
    }

    public static boolean isAdmin() {
        return hasRole(Role.ADMIN);
    }
}
