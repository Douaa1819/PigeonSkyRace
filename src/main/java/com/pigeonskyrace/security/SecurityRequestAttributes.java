package com.pigeonskyrace.security;

/**
 * Request attribute keys used between the JWT filter and the authentication entry point.
 */
public final class SecurityRequestAttributes {

    public static final String JWT_EXPIRED = "com.pigeonskyrace.security.JWT_EXPIRED";

    private SecurityRequestAttributes() {
    }
}
