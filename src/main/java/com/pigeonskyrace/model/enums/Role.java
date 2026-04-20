package com.pigeonskyrace.model.enums;

/**
 * Application roles. Stored in MongoDB as enum name (e.g. BREEDER).
 * Spring Security expects {@code ROLE_*} authorities — use {@link #getAuthority()}.
 */
public enum Role {
    ADMIN,
    ORGANIZER,
    BREEDER;

    public String getAuthority() {
        return "ROLE_" + name();
    }
}
